package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.IntroBoardGroup;
import server.inuappcenter.kr.data.domain.IntroBoardStack;
import server.inuappcenter.kr.data.domain.Stack;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.FaqBoard;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.GroupResponseDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;
import server.inuappcenter.kr.data.dto.response.StackResponseDto;
import server.inuappcenter.kr.data.redis.repository.ImageRedisRepository;
import server.inuappcenter.kr.data.redis.repository.BoardResponseRedisRepository;
import server.inuappcenter.kr.data.repository.*;
import server.inuappcenter.kr.exception.customExceptions.CustomFileSizeMisMatchException;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository<Board> boardRepository;
    private final ImageRepository imageRepository;
    private final HttpServletRequest request;
    private final BoardResponseRedisRepository<BoardResponseDto> boardResponseRedisRepository;
    private final ImageRedisRepository imageRedisRepository;
    private final StackRepository stackRepository;
    private final GroupRepository groupRepository;
    private final IntroBoardStackRepository introBoardStackRepository;
    private final IntroBoardGroupRepository introBoardGroupRepository;

    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(Long id) {
        return boardResponseRedisRepository.findById(id).orElseGet(
                () -> {
                    Board result = boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 보드가 없습니다."));
                    BoardResponseDto resultDto = result.createResponse(request);

                    // IntroBoard인 경우 stacks, groups 조회 후 재구성
                    if (result instanceof IntroBoard) {
                        resultDto = buildIntroBoardResponse((IntroBoard) result, (IntroBoardResponseDto) resultDto);
                    }

                    boardResponseRedisRepository.save(resultDto);
                    return resultDto;
                }
        );
    }

    private IntroBoardResponseDto buildIntroBoardResponse(IntroBoard introBoard, IntroBoardResponseDto baseResponse) {
        List<StackResponseDto> stacks = introBoardStackRepository.findAllByIntroBoard(introBoard).stream()
                .map(ibs -> StackResponseDto.entityToDto(ibs.getStack(), request))
                .collect(Collectors.toList());

        List<GroupResponseDto> groups = introBoardGroupRepository.findAllByIntroBoard(introBoard).stream()
                .map(ibg -> GroupResponseDto.toIntroBoardDto(ibg.getGroup()))
                .collect(Collectors.toList());

        return IntroBoardResponseDto.builder()
                .id(baseResponse.getId())
                .title(baseResponse.getTitle())
                .subTitle(baseResponse.getSubTitle())
                .androidStoreLink(baseResponse.getAndroidStoreLink())
                .appleStoreLink(baseResponse.getAppleStoreLink())
                .websiteLink(baseResponse.getWebsiteLink())
                .isActive(baseResponse.getIsActive())
                .body(baseResponse.getBody())
                .images(baseResponse.getImages())
                .stacks(stacks)
                .groups(groups)
                .createdDate(baseResponse.getCreatedDate())
                .lastModifiedDate(baseResponse.getLastModifiedDate())
                .build();
    }


    @Transactional
    public CommonResponseDto saveBoard(BoardRequestDto boardRequestDto) {
        Board savedBoard = boardRepository.save(boardRequestDto.createBoard());

        // IntroBoard인 경우 Stack, Group 연결 처리
        if (savedBoard instanceof IntroBoard && boardRequestDto instanceof IntroBoardRequestDto) {
            IntroBoard introBoard = (IntroBoard) savedBoard;
            IntroBoardRequestDto introBoardRequestDto = (IntroBoardRequestDto) boardRequestDto;
            saveIntroBoardRelations(introBoard, introBoardRequestDto);
        }

        return new CommonResponseDto(savedBoard.getId() + " Board has been successfully saved.");
    }

    private void saveIntroBoardRelations(IntroBoard introBoard, IntroBoardRequestDto requestDto) {
        // Stack 연결
        if (requestDto.getStackIds() != null && !requestDto.getStackIds().isEmpty()) {
            for (Long stackId : requestDto.getStackIds()) {
                Stack stack = stackRepository.findById(stackId)
                        .orElseThrow(() -> new CustomNotFoundException("Stack ID: " + stackId + "를 찾을 수 없습니다."));
                introBoardStackRepository.save(new IntroBoardStack(introBoard, stack));
            }
        }

        // Group 연결
        if (requestDto.getGroupIds() != null && !requestDto.getGroupIds().isEmpty()) {
            for (Long groupId : requestDto.getGroupIds()) {
                Group group = groupRepository.findById(groupId)
                        .orElseThrow(() -> new CustomNotFoundException("Group ID: " + groupId + "를 찾을 수 없습니다."));
                introBoardGroupRepository.save(new IntroBoardGroup(introBoard, group));
            }
        }
    }

    @Transactional
    public CommonResponseDto deleteBoard(Long id) {
        boardResponseRedisRepository.deleteById(id);
        boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 보드가 없습니다."));
        boardRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

    @Transactional
    public CommonResponseDto updateBoard(Long board_id, List<Long> image_id, BoardRequestDto boardRequestDto) {
        // 캐시에서 보드를 삭제한다.
        boardResponseRedisRepository.deleteById(board_id);

        Board foundBoard = boardRepository.findById(board_id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        // Board 타입과 RequestDto 타입 일치 여부 확인
        validateBoardAndRequestDtoType(foundBoard, boardRequestDto);

        List<MultipartFile> multipartFiles = boardRequestDto.getMultipartFiles();
        // 사용자가 multipart를 같이 보냈는지 확인
        boolean hasMultipartFiles = multipartFiles != null && !multipartFiles.isEmpty();
        boolean hasImageIds = image_id != null && !image_id.isEmpty();

        if (hasMultipartFiles || hasImageIds) {
            // 둘 다 있는 경우: 크기 비교 (기존 이미지 수정)
            if (hasMultipartFiles && hasImageIds) {
                if (image_id.size() != multipartFiles.size()) {
                    throw new CustomFileSizeMisMatchException("photo_ids와 files의 크기가 다릅니다");
                }
            }
            // 요청한 이미지만 있고 ID가 없는 경우: 새 이미지 추가
            else if (hasMultipartFiles && !hasImageIds) {
                List<Image> newImages = new ArrayList<>();
                for (MultipartFile file : multipartFiles) {
                    newImages.add(new Image(file));
                }
                foundBoard.updateImage(newImages);
                imageRepository.saveAll(newImages);
                foundBoard.modifyBoard(boardRequestDto);
                boardRepository.save(foundBoard);

                // IntroBoard인 경우 Stack, Group 연결 업데이트
                if (foundBoard instanceof IntroBoard && boardRequestDto instanceof IntroBoardRequestDto) {
                    IntroBoard introBoard = (IntroBoard) foundBoard;
                    IntroBoardRequestDto introBoardRequestDto = (IntroBoardRequestDto) boardRequestDto;
                    updateIntroBoardRelations(introBoard, introBoardRequestDto);
                }

                return new CommonResponseDto("id: " + board_id + " has been successfully modified.");
            }
            
            // 이미지 레포지토리에서 사용자가 보낸 ID로 조회를 먼저 진행하여, 찾아진 이미지 목록을 가짐
            List<Image> foundImageList = imageRepository.findByImageIdsAndBoard(image_id, foundBoard);

            // foundImageList에 있는 image의 id를 따로 뽑아서 저장
            List<Long> foundImageIds = new ArrayList<>();
            for (Image image : foundImageList) {
                // 찾아진 이미지 목록에서 id를 가져와 찾아진 id 목록에 추가함
                foundImageIds.add(image.getId());
            }
            // 캐시에서 이미지를 삭제한다.
            imageRedisRepository.deleteAllById(foundImageIds);

            // DB에 존재하지 않는 id(=새로 추가할 이미지의 id)를 찾음
            List<Long> missingImageIds = new ArrayList<>();
            for (Long id : image_id) {
                // 찾아진 Id 목록에 사용자가 보낸 ID가 존재하지 않는다면
                if (!foundImageIds.contains(id)) {
                    // 이 ID를 없는 이미지 ID에 추가함
                    missingImageIds.add(id);
                }
            }

            // 인덱스 맵핑 -> 수정과 추가가 섞일시 누락 발생에 대하여
            // image_id와 multipartFiles가 같은 인덱스로 매핑
            // image_id의 순서가 저장되어 있는 multipartFiles의 순서가 다를 수 있음.
            // 수정을 먼저 하고, 추가를 나중에 진행

            // 기존 이미지 인덱스 매핑 -> 이미지 수정이 함께 일어남.
            for (Image image : foundImageList) {
                Long currentImageId = image.getId();
                int idx = image_id.indexOf(currentImageId);
                if(idx != -1) {
                    MultipartFile updateFile = boardRequestDto.getMultipartFiles().get(idx);
                    image.updateImage(updateFile);
                }
            }

            // 새로운 이미지 리스트 생성
            List<Image> newImageList = new ArrayList<>();
            for(Long newImageId : missingImageIds) {
                int idx = image_id.indexOf(newImageId);
                if(idx != -1) {
                    MultipartFile newImageFile = boardRequestDto.getMultipartFiles().get(idx);
                    Image newImage = new Image(newImageFile);
                    newImageList.add(newImage);
                }
            }

            // 해당 board에 새로운 이미지 추가
            if (!newImageList.isEmpty()) {
                foundBoard.updateImage(newImageList);
            }

            imageRepository.saveAll(foundImageList);
            imageRepository.saveAll(newImageList);
        }
        // 이미지가 없을 경우 글 내용만 수정한다.
        foundBoard.modifyBoard(boardRequestDto);
        boardRepository.save(foundBoard);

        // IntroBoard인 경우 Stack, Group 연결 업데이트
        if (foundBoard instanceof IntroBoard && boardRequestDto instanceof IntroBoardRequestDto) {
            IntroBoard introBoard = (IntroBoard) foundBoard;
            IntroBoardRequestDto introBoardRequestDto = (IntroBoardRequestDto) boardRequestDto;
            updateIntroBoardRelations(introBoard, introBoardRequestDto);
        }

        return new CommonResponseDto("id: " + board_id + " has been successfully modified.");
    }

    private void updateIntroBoardRelations(IntroBoard introBoard, IntroBoardRequestDto requestDto) {
        // Stack
        if (requestDto.getStackIds() != null) {
            List<IntroBoardStack> existingStacks = introBoardStackRepository.findAllByIntroBoard(introBoard);
            List<Long> existingStackIds = existingStacks.stream()
                    .map(ids -> ids.getStack().getId())
                    .collect(Collectors.toList());
            List<Long> requestedStackIds = requestDto.getStackIds();

            // 요청과 db 다를시 삭제
            for (IntroBoardStack existing : existingStacks) {
                if (!requestedStackIds.contains(existing.getStack().getId())) {
                    introBoardStackRepository.delete(existing);
                }
            }

            // 추가 스택 저장
            for (Long stackId : requestedStackIds) {
                if (!existingStackIds.contains(stackId)) {
                    Stack stack = stackRepository.findById(stackId)
                            .orElseThrow(() -> new CustomNotFoundException("Stack ID: " + stackId + "를 찾을 수 없습니다."));
                    introBoardStackRepository.save(new IntroBoardStack(introBoard, stack));
                }
            }
        }

        // Group
        if (requestDto.getGroupIds() != null) {
            List<IntroBoardGroup> existingGroups = introBoardGroupRepository.findAllByIntroBoard(introBoard);
            List<Long> existingGroupIds = existingGroups.stream()
                    .map(ibg -> ibg.getGroup().getGroup_id())
                    .collect(Collectors.toList());
            List<Long> requestedGroupIds = requestDto.getGroupIds();

            for (IntroBoardGroup existing : existingGroups) {
                if (!requestedGroupIds.contains(existing.getGroup().getGroup_id())) {
                    introBoardGroupRepository.delete(existing);
                }
            }

            for (Long groupId : requestedGroupIds) {
                if (!existingGroupIds.contains(groupId)) {
                    Group group = groupRepository.findById(groupId)
                            .orElseThrow(() -> new CustomNotFoundException("Group ID: " + groupId + "를 찾을 수 없습니다."));
                    introBoardGroupRepository.save(new IntroBoardGroup(introBoard, group));
                }
            }
        }
    }

    @Transactional
    public CommonResponseDto updateAppActivation(Long id, Boolean isActive) {
        boardResponseRedisRepository.deleteById(id);
        Board foundBoard = boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 보드가 없습니다."));
        
        if (foundBoard instanceof IntroBoard) {
            IntroBoard introBoard = (IntroBoard) foundBoard;
            introBoard.isActive = isActive;
            boardRepository.save(introBoard);
            return new CommonResponseDto("App activation status updated to: " + isActive);
        } else {
            throw new CustomNotFoundException("해당 게시글은 앱 소개 게시글이 아닙니다.");
        }
    }

    private void validateBoardAndRequestDtoType(Board board, BoardRequestDto requestDto) {
        String boardType = board.getClass().getSimpleName();
        String requestDtoType = requestDto.getClass().getSimpleName();

        if (board instanceof IntroBoard && !(requestDto instanceof IntroBoardRequestDto)) {
            throw new IllegalArgumentException(
                    "Board 타입 불일치: " + boardType + "은(는) IntroBoardRequestDto로만 수정 가능합니다. (요청된 DTO: " + requestDtoType + ")");
        }
        if (board instanceof PhotoBoard && !(requestDto instanceof PhotoBoardRequestDto)) {
            throw new IllegalArgumentException(
                    "Board 타입 불일치: " + boardType + "은(는) PhotoBoardRequestDto로만 수정 가능합니다. (요청된 DTO: " + requestDtoType + ")");
        }
        if (board instanceof FaqBoard && !(requestDto instanceof FaqBoardRequestDto)) {
            throw new IllegalArgumentException(
                    "Board 타입 불일치: " + boardType + "은(는) FaqBoardRequestDto로만 수정 가능합니다. (요청된 DTO: " + requestDtoType + ")");
        }
    }
}