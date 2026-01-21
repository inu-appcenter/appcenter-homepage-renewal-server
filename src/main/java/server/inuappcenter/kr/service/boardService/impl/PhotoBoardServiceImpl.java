package server.inuappcenter.kr.service.boardService.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.redis.repository.BoardResponseRedisRepository;
import server.inuappcenter.kr.data.redis.repository.ImageRedisRepository;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.repository.PhotoBoardRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service("PhotoBoardServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class PhotoBoardServiceImpl implements AdditionalBoardService {
    private final PhotoBoardRepository photoBoardRepository;
    private final ImageRepository imageRepository;
    private final BoardResponseRedisRepository<BoardResponseDto> boardResponseRedisRepository;
    private final ImageRedisRepository imageRedisRepository;
    private final HttpServletRequest request;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findBoardList(String topic) {
        List<BoardResponseDto> responseDtoList= new ArrayList<>();
        for (Board board : photoBoardRepository.findAll()) {
            responseDtoList.add(board.createResponse(request));
        }
        return responseDtoList;
    }

    @Transactional
    public CommonResponseDto updateBoard(Long boardId, Long imageId, PhotoBoardRequestDto requestDto) {
        boardResponseRedisRepository.deleteById(boardId);

        PhotoBoard foundBoard = photoBoardRepository.findById(boardId)
                .orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        MultipartFile multipartFile = requestDto.getMultipartFile();

        if (multipartFile != null && !multipartFile.isEmpty()) {
            if (imageId != null) {
                // 기존 이미지 수정
                Image foundImage = imageRepository.findById(imageId)
                        .orElseThrow(() -> new CustomNotFoundException("Image ID: " + imageId + "를 찾을 수 없습니다."));
                imageRedisRepository.deleteById(imageId);
                foundImage.updateImage(multipartFile);
                imageRepository.save(foundImage);
            } else {
                // 새 이미지 추가 (기존 이미지 교체)
                List<Image> newImageList = new ArrayList<>();
                newImageList.add(new Image(multipartFile));
                foundBoard.updateImage(newImageList);
            }
        }

        foundBoard.updateBoard(requestDto);
        photoBoardRepository.save(foundBoard);

        return new CommonResponseDto("id: " + boardId + " has been successfully modified.");
    }
}
