package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.repository.PhotoBoardRepository;
import server.inuappcenter.kr.data.utils.BoardUtils;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoBoardService {
    private final PhotoBoardRepository photoBoardRepository;
    private final ImageRepository imageRepository;
    private final HttpServletRequest request;

    @Transactional(readOnly = true)
    public PhotoBoardResponseDto getPhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return PhotoBoardResponseDto.entityToDto(request, foundBoard);
    }

    @Transactional
    public PhotoBoardResponseDto updatePhotoBoard(PhotoBoardRequestDto photoBoardRequestDto, Long board_id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(board_id).orElseThrow(()-> new CustomNotFoundException("The requested ID was not found."));
        foundBoard.updateBoard(photoBoardRequestDto);
        List<Image> foundImg = imageRepository.findByPhotoBoard(foundBoard);

        // 찾은 이미지에 내용 대입
        for(Image image: foundImg) {
            for(MultipartFile multipartFile: photoBoardRequestDto.getMultipartFiles()) {
                image.setImage(multipartFile);
            }
        }

        PhotoBoard photoBoard = photoBoardRepository.save(foundBoard);
        List<Image> savedImage = imageRepository.saveAll(foundImg);

        return PhotoBoardResponseDto.entityToDto(request, photoBoard);
    }

    @Transactional
    public List<PhotoBoardResponseDto> findAllPhotoBoard() {
        List<PhotoBoard> boardList = photoBoardRepository.findAll();
        List<Image> thumbnailList = new ArrayList<>();
        for (PhotoBoard photoBoard : boardList) {
            for (int j = 0; j < photoBoard.getImages().size(); j++) {
                if (photoBoard.getImages().get(j).getIsThumbnail()) {
                    thumbnailList.add(photoBoard.getImages().get(j));
                }
            }
        }

        return BoardUtils.returnPhotoBoardResponseDtoList(boardList, thumbnailList, request);
    }



}
