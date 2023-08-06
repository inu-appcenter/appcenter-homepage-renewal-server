package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.PhotoBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.BoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.ImageRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.BoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.ImageRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.PhotoBoardRepository;
import javax.servlet.http.HttpServletRequest;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.utils.BoardUtils;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.service.CustomNotFoundIdException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class PhotoBoardService extends BoardUtils {
    private final PhotoBoardRepository photoBoardRepository;
    private final ImageRepository imageRepository;
    private final HttpServletRequest request;

    public BoardResponseDto<List<String>> getBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);

        List<Image> ImageList = foundBoard.getImages();

        BoardResponseDto<List<String>> boardResponseDto = new BoardResponseDto<>();
        boardResponseDto.setBoardResponse(foundBoard, super.returnImageURL(request, ImageList));
        return boardResponseDto;
    }

    @Transactional
    // 게시글 저장하기
    public BoardResponseDto<List<Long>> saveBoard(BoardRequestDto boardRequestDto, ImageRequestDto imageRequestDto) {
        PhotoBoard photoBoard = new PhotoBoard();
        // imageRequestDto를 List<Image> 타입으로 변환 / 게시판 정보도 함께 포함해서 저장시킴
        List<Image> imageList = new <PhotoBoard>Image().toList(imageRequestDto, photoBoard);

        // introBoardRequestDto를 introBoard 타입으로 변환
        photoBoard.setPhotoBoard(boardRequestDto);

        // introBoard를 저장
        photoBoardRepository.save(photoBoard);

        // 첫번째 이미지는 isThumbnail을 true로 변경
        imageList.get(0).isThumbnail();

        List<Image> savedImage = imageRepository.saveAll(imageList);

        List<Long> imageIds = new ArrayList<>();

        for(Image image: savedImage) {
            imageIds.add(image.getId());
        }

        BoardResponseDto<List<Long>> boardResponseDto = new BoardResponseDto<>();
        boardResponseDto.setBoardResponse(photoBoard, imageIds);
        return boardResponseDto;
    }

    public String deleteBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        photoBoardRepository.deleteById(id);

        return "id: " + id + " has been successfully deleted.";
    }

    public List<BoardResponseDto<String>> findAllBoard() {
        List<PhotoBoard> boardList = photoBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return super.returnPhotoBoardResponseDtoList(boardList, thumbnailList, request);
    }
}
