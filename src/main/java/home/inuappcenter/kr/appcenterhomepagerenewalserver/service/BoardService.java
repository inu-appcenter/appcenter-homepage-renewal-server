package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Board;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.PhotoBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.ImageRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.PhotoBoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.IntroBoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.PhotoBoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.IntroBoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.BoardRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.ImageRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.IntroBoardRepository;
import javax.servlet.http.HttpServletRequest;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.PhotoBoardRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.utils.BoardUtils;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.service.CustomNotFoundIdException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BoardService extends BoardUtils {
    private final BoardRepository<Board> boardRepository;

    private final IntroBoardRepository introBoardRepository;
    private final PhotoBoardRepository photoBoardRepository;

    private final ImageRepository imageRepository;
    private final HttpServletRequest request;

    @Transactional
    // (앱) 게시글 조회하기
    public IntroBoardResponseDto<List<String>> getIntroBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);

        List<Image> ImageList = foundBoard.getImages();

        IntroBoardResponseDto<List<String>> boardResponseDto = new IntroBoardResponseDto<>();
        boardResponseDto.setIntroBoardResponse(foundBoard, super.returnImageURL(request, ImageList));
        return boardResponseDto;
    }

    @Transactional
    // (앱) 게시글 저장하기
    public IntroBoardResponseDto<List<Long>> saveIntroBoard(IntroBoardRequestDto introBoardRequestDto, ImageRequestDto imageRequestDto) {
        IntroBoard introBoard = new IntroBoard();
        // introBoardRequestDto를 introBoard 타입으로 변환
        introBoard.setIntroBoard(introBoardRequestDto);

        // imageRequestDto를 List<Image> 타입으로 변환 / 게시판 정보도 함께 포함해서 저장시킴
        List<Image> imageList = new <IntroBoard>Image().toList(imageRequestDto, introBoard);

        // introBoard를 저장
        boardRepository.save(introBoard);

        // 첫번째 이미지는 isThumbnail을 true로 변경
        imageList.get(0).isThumbnail();

        List<Image> savedImage = imageRepository.saveAll(imageList);

        IntroBoardResponseDto<List<Long>> introBoardResponseDto = new IntroBoardResponseDto<>();
        introBoardResponseDto.setIntroBoardResponse(introBoard, super.returnImageId(savedImage));
        return introBoardResponseDto;
    }

    @Transactional
    // (앱) 게시글 수정
    public IntroBoardResponseDto<List<Long>> updateIntroBoard(IntroBoardRequestDto introBoardRequestDto, ImageRequestDto imageRequestDto, Long board_id) {
        // 보드 찾아오기
        IntroBoard foundBoard = introBoardRepository.findById(board_id).orElseThrow(()-> new RuntimeException(""));
        // 찾은 보드에 바뀐 내용 대입
        foundBoard.setIntroBoard(introBoardRequestDto);

        // 이미지를 found 해옴
        List<Image> foundImg = imageRepository.findByIntroBoard(foundBoard);
        // 찾은 이미지에 내용 대입
        for(Image image: foundImg) {
            for(MultipartFile multipartFile: imageRequestDto.getMultipartFileList()) {
                image.setImage(multipartFile);
            }

        }

        IntroBoard introBoard = introBoardRepository.save(foundBoard);

        List<Image> savedImage = imageRepository.saveAll(foundImg);

        IntroBoardResponseDto<List<Long>> introBoardResponseDto = new IntroBoardResponseDto<>();
        introBoardResponseDto.setIntroBoardResponse(introBoard, super.returnImageId(savedImage));
        return introBoardResponseDto;

    }

    @Transactional
    // (앱) 게시글 삭제
    public String deleteIntroBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow();

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        introBoardRepository.deleteById(id);

        return "id: " + id + " has been successfully deleted.";
    }

    @Transactional
    // (앱) 모든 게시글 조회하기
    public List<IntroBoardResponseDto<String>> findAllIntroBoard() {
        List<IntroBoard> boardList = introBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return super.returnIntroBoardResponseDtoList(boardList, thumbnailList, request);
    }

    @Transactional
    // (사진) 게시글 조회
    public PhotoBoardResponseDto<List<String>> getPhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);

        List<Image> ImageList = foundBoard.getImages();

        PhotoBoardResponseDto<List<String>> photoBoardResponseDto = new PhotoBoardResponseDto<>();
        photoBoardResponseDto.setPhotoBoardResponse(foundBoard, super.returnImageURL(request, ImageList));
        return photoBoardResponseDto;
    }

    @Transactional
    // (사진) 게시글 저장
    public PhotoBoardResponseDto<List<Long>> savePhotoBoard(PhotoBoardRequestDto photoBoardRequestDto, ImageRequestDto imageRequestDto) {
        PhotoBoard photoBoard = new PhotoBoard();
        // imageRequestDto를 List<Image> 타입으로 변환 / 게시판 정보도 함께 포함해서 저장시킴
        List<Image> imageList = new <PhotoBoard>Image().toList(imageRequestDto, photoBoard);

        // introBoardRequestDto를 introBoard 타입으로 변환
        photoBoard.setPhotoBoard(photoBoardRequestDto);

        // introBoard를 저장
        boardRepository.save(photoBoard);

        // 첫번째 이미지는 isThumbnail을 true로 변경
        imageList.get(0).isThumbnail();

        List<Image> savedImage = imageRepository.saveAll(imageList);

        List<Long> imageIds = new ArrayList<>();

        for(Image image: savedImage) {
            imageIds.add(image.getId());
        }

        PhotoBoardResponseDto<List<Long>> photoBoardResponseDto = new PhotoBoardResponseDto<>();
        photoBoardResponseDto.setPhotoBoardResponse(photoBoard, imageIds);
        return photoBoardResponseDto;
    }

    @Transactional
    // (사진) 게시글 삭제
    public String deletePhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        boardRepository.deleteById(id);

        return "id: " + id + " has been successfully deleted.";
    }

    @Transactional
    // (사진) 모든 게시글 조회하기
    public List<PhotoBoardResponseDto<String>> findAllPhotoBoard() {
        List<PhotoBoard> boardList = photoBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return super.returnPhotoBoardResponseDtoList(boardList, thumbnailList, request);
    }
}
