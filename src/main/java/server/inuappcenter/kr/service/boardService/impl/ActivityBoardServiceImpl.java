package server.inuappcenter.kr.service.boardService.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.board.ActivityBoard;
import server.inuappcenter.kr.data.domain.board.ActivityContent;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.dto.request.ActivityBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.ContentRequestDto;
import server.inuappcenter.kr.data.dto.response.ActivityBoardResponseDto;
import server.inuappcenter.kr.data.dto.response.ActivityContentResponseDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.redis.repository.BoardResponseRedisRepository;
import server.inuappcenter.kr.data.redis.repository.ImageRedisRepository;
import server.inuappcenter.kr.data.repository.ActivityBoardRepository;
import server.inuappcenter.kr.data.repository.ActivityContentRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;
import server.inuappcenter.kr.exception.customExceptions.CustomFileSizeMisMatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service("ActivityBoardServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class ActivityBoardServiceImpl implements AdditionalBoardService {
    private final ActivityBoardRepository activityBoardRepository;
    private final ActivityContentRepository activityContentRepository;
    private final BoardResponseRedisRepository<BoardResponseDto> boardResponseRedisRepository;
    private final ImageRedisRepository imageRedisRepository;
    private final HttpServletRequest request;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findBoardList(String topic) {
        List<BoardResponseDto> responseDtoList = new ArrayList<>();
        for (ActivityBoard board : activityBoardRepository.findAll()) {
            responseDtoList.add(buildResponse(board));
        }
        return responseDtoList;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(Long id) {
        return boardResponseRedisRepository.findById(id)
                .orElseGet(() -> {
                    ActivityBoard board = activityBoardRepository.findById(id)
                            .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 활동 게시글이 없습니다."));
                    ActivityBoardResponseDto responseDto = buildResponse(board);
                    boardResponseRedisRepository.save(responseDto);
                    log.info("ActivityBoard Redis 캐시 저장: id={}", id);
                    return responseDto;
                });
    }

    private ActivityBoardResponseDto buildResponse(ActivityBoard board) {
        List<ActivityContent> contents = activityContentRepository.findAllByActivityBoardOrderBySequenceAsc(board);
        List<ActivityContentResponseDto> contentDtos = contents.stream()
                .map(content -> ActivityContentResponseDto.entityToDto(content, request))
                .collect(java.util.stream.Collectors.toList());

        String imageUrl = null;
        if (board.getThumbnail() != null) {
            imageUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/photo/" + board.getThumbnail().getId();
        }
        return ActivityBoardResponseDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .titleEng(board.getTitleEng())
                .body(board.getBody())
                .thumbnail(imageUrl)
                .author(board.getAuthor())
                .contents(contentDtos)
                .createdDate(board.getCreatedDate())
                .lastModifiedDate(board.getLastModifiedDate())
                .build();
    }

    @Transactional
    public CommonResponseDto saveBoard(ActivityBoardRequestDto requestDto) {
        ActivityBoard board = new ActivityBoard(requestDto);
        ActivityBoard savedBoard = activityBoardRepository.save(board);

        // Contents 추가
        if (requestDto.getContents() != null) {
            for (ContentRequestDto contentDto : requestDto.getContents()) {
                ActivityContent content = new ActivityContent(
                        contentDto.getSubTitle(),
                        contentDto.getText(),
                        contentDto.getImages(),
                        contentDto.getSequence(),
                        savedBoard
                );
                activityContentRepository.save(content);
            }
        }

        return new CommonResponseDto(savedBoard.getId() + " Activity Board has been successfully saved.");
    }

    @Transactional
    public CommonResponseDto updateBoard(Long boardId, ActivityBoardRequestDto requestDto) {
        ActivityBoard board = activityBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 활동 게시글이 없습니다."));

        // 기본 정보 업데이트
        board.updateBoard(requestDto);

        // 실제로는 이미지 업데이트는 -> 다른 로직에서 진행
        //board.updateThumbnail(requestDto.getThumbnail());

        // Contents 업데이트 (ID 유지, 이미지 바이너리만 교체 가능)
        if (requestDto.getContents() != null) {
            List<ActivityContent> existingContents = activityContentRepository.findAllByActivityBoardOrderBySequenceAsc(board);
            Map<Long, ActivityContent> existingById = new HashMap<>();
            for (ActivityContent content : existingContents) {
                if (content.getId() != null) {
                    existingById.put(content.getId(), content);
                }
            }

            for (ContentRequestDto contentDto : requestDto.getContents()) {
                Long contentId = contentDto.getContentId();
                if (contentId != null && existingById.containsKey(contentId)) {
                    ActivityContent existing = existingById.get(contentId);
                    existing.updateContent(
                            contentDto.getSubTitle(),
                            contentDto.getText()
                    );
                } else {
                    ActivityContent content = new ActivityContent(
                            contentDto.getSubTitle(),
                            contentDto.getText(),
                            contentDto.getImages(),
                            contentDto.getSequence(),
                            board
                    );
                    activityContentRepository.save(content);
                }
            }
        }

        activityBoardRepository.save(board);
        boardResponseRedisRepository.deleteById(boardId);
        log.info("ActivityBoard Redis 캐시 삭제: id={}", boardId);
        return new CommonResponseDto("id: " + boardId + " has been successfully modified.");
    }

    @Transactional
    public CommonResponseDto updateThumbnail(Long boardId, MultipartFile thumbnail) {
        ActivityBoard board = activityBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 활동 게시글이 없습니다."));

        if (thumbnail == null || thumbnail.isEmpty()) {
            throw new IllegalArgumentException("대표 이미지가 비어있을 수 없습니다.");
        }


        Long oldThumbnailId = board.getThumbnail() != null ? board.getThumbnail().getId() : null;

        board.updateThumbnail(thumbnail);
        activityBoardRepository.save(board);

        // Redis 캐시 삭제 (이미지 + 보드)
        if (oldThumbnailId != null) {
            imageRedisRepository.deleteById(oldThumbnailId);
            log.info("썸네일 이미지 Redis 캐시 삭제: imageId={}", oldThumbnailId);
        }
        boardResponseRedisRepository.deleteById(boardId);
        log.info("ActivityBoard Redis 캐시 삭제: id={}", boardId);

        log.info("썸네일 수정 완료: boardId={}, thumbnailId={}",
                boardId,
                board.getThumbnail() != null ? board.getThumbnail().getId() : "없음");

        return new CommonResponseDto("thumbnail has been successfully modified.");
    }

    @Transactional
    public CommonResponseDto updateContentImages(Long contentId, List<Long> imageIds, List<MultipartFile> files) {
        ActivityContent content = activityContentRepository.findById(contentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 활동 콘텐츠가 없습니다."));

        boolean hasFiles = files != null && !files.isEmpty();
        boolean hasImageIds = imageIds != null && !imageIds.isEmpty();

        if (!hasFiles) {
            throw new IllegalArgumentException("이미지 파일이 비어있을 수 없습니다.");
        }

        // image_ids + files: 기존 이미지 교체 (개수 일치 필요)
        if (hasImageIds) {
            if (imageIds.size() != files.size()) {
                throw new CustomFileSizeMisMatchException("image_ids와 files의 크기가 다릅니다.");
            }

            Map<Long, Image> existingMap = new HashMap<>();
            for (Image image : content.getImages()) {
                existingMap.put(image.getId(), image);
            }

            for (int i = 0; i < imageIds.size(); i++) {
                Image target = existingMap.get(imageIds.get(i));
                if (target == null) {
                    throw new CustomNotFoundException("ID " + imageIds.get(i) + "에 해당하는 이미지가 없습니다.");
                }
                target.updateImage(files.get(i));
                // Redis 캐시 삭제
                imageRedisRepository.deleteById(imageIds.get(i));
            }
        }
        // files만: 새 이미지 추가
        else {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    content.getImages().add(new Image(file));
                }
            }
        }

        // 보드 Redis 캐시 삭제
        invalidateBoardCache(content.getActivityBoard());

        return new CommonResponseDto("content images have been successfully updated.");
    }

    @Transactional
    public CommonResponseDto deleteContentImages(Long contentId, List<Long> imageIds) {
        ActivityContent content = activityContentRepository.findById(contentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 활동 콘텐츠가 없습니다."));

        boolean removed = false;
        if (content.getImages() != null && !content.getImages().isEmpty()) {
            for (int i = content.getImages().size() - 1; i >= 0; i--) {
                if (imageIds.contains(content.getImages().get(i).getId())) {
                    content.getImages().remove(i);
                    removed = true;
                }
            }
        }

        if (!removed) {
            throw new CustomNotFoundException("삭제할 이미지가 없습니다.");
        }

        // 보드 Redis 캐시 삭제
        invalidateBoardCache(content.getActivityBoard());

        return new CommonResponseDto("successfully deleted.");
    }

    @Transactional
    public CommonResponseDto reorderContentImages(Long contentId, List<Long> imageIds) {
        ActivityContent content = activityContentRepository.findById(contentId)
                .orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 활동 콘텐츠가 없습니다."));

        if (imageIds == null || imageIds.isEmpty()) {
            throw new IllegalArgumentException("이미지 ID가 비어있을 수 없습니다.");
        }

        List<Image> existingImages = content.getImages();
        if (existingImages == null || existingImages.isEmpty()) {
            throw new CustomNotFoundException("정렬할 이미지가 없습니다.");
        }

        // 총 갯수가 같은지 비교
        if (imageIds.size() != existingImages.size()) {
            throw new IllegalArgumentException("이미지 ID 개수가 기존 이미지 개수와 일치해야 합니다.");
        }

        Map<Long, Image> imageMap = new HashMap<>();
        for (Image image : existingImages) {
            imageMap.put(image.getId(), image);
        }

        if (!imageMap.keySet().containsAll(imageIds)) {
            throw new CustomNotFoundException("요청한 이미지가 존재하지 않습니다.");
        }

        List<Image> reordered = new ArrayList<>();
        Set<Long> seen = new HashSet<>();
        for (Long id : imageIds) {
            if (!seen.add(id)) {
                throw new IllegalArgumentException("중복된 이미지 ID가 있습니다.");
            }
            reordered.add(imageMap.get(id));
        }

        // clear() + addAll() 대신 set()으로 교체 (orphanRemoval 삭제 방지)
        for (int i = 0; i < reordered.size(); i++) {
            existingImages.set(i, reordered.get(i));
        }

        // 보드 Redis 캐시 삭제
        invalidateBoardCache(content.getActivityBoard());

        return new CommonResponseDto("content images have been successfully reordered.");
    }

    private void invalidateBoardCache(ActivityBoard board) {
        if (board != null && board.getId() != null) {
            boardResponseRedisRepository.deleteById(board.getId());
            log.info("ActivityBoard Redis 캐시 삭제: id={}", board.getId());
        }
    }
}
