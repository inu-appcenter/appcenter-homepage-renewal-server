package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.ImageRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.BoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.BoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.ImageRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.IntroBoardRepository;
import javax.servlet.http.HttpServletRequest;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.service.CustomNotFoundIdException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class IntroBoardService {
    private final IntroBoardRepository introBoardRepository;
    private final ImageRepository imageRepository;
    private final HttpServletRequest request;

    @Transactional
    // 게시글 조회하기
    public BoardResponseDto<List<String>> getBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow(CustomNotFoundIdException::new);

        List<Image> ImageList = foundBoard.getImages();

        List<String> images = new ArrayList<>();

        for(Image image: ImageList) {
            images.add(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  "/image/photo/" + image.getId().toString());
        }

        BoardResponseDto<List<String>> boardResponseDto = new BoardResponseDto<>();
        boardResponseDto.setBoardResponse(foundBoard, images);
        return boardResponseDto;
    }

    @Transactional
    // 게시글 저장하기
    public BoardResponseDto<List<Long>> saveBoard(BoardRequestDto boardRequestDto, ImageRequestDto imageRequestDto) {
        IntroBoard introBoard = new IntroBoard();
        // introBoardRequestDto를 introBoard 타입으로 변환
        introBoard.setIntroBoard(boardRequestDto);

        // imageRequestDto를 List<Image> 타입으로 변환 / 게시판 정보도 함께 포함해서 저장시킴
        List<Image> imageList = new <IntroBoard>Image().toList(imageRequestDto, introBoard);

        // introBoard를 저장
        introBoardRepository.save(introBoard);

        // 첫번째 이미지는 isThumbnail을 true로 변경
        imageList.get(0).isThumbnail();

        List<Image> savedImage = imageRepository.saveAll(imageList);

        List<Long> imageIds = new ArrayList<>();

        for(Image image: savedImage) {
            imageIds.add(image.getId());
        }

        BoardResponseDto<List<Long>> boardResponseDto = new BoardResponseDto<>();
        boardResponseDto.setBoardResponse(introBoard, imageIds);
        return boardResponseDto;
    }

    @Transactional
    // 게시글 수정
    public BoardResponseDto<List<Long>> updateBoard(BoardRequestDto boardRequestDto, ImageRequestDto imageRequestDto, Long board_id) {
        // 보드 찾아오기
        IntroBoard foundBoard = introBoardRepository.findById(board_id).orElseThrow(()-> new RuntimeException(""));
        // 찾은 보드에 바뀐 내용 대입
        foundBoard.setIntroBoard(boardRequestDto);

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

        List<Long> imageIds = new ArrayList<>();

        for(Image image: savedImage) {
            imageIds.add(image.getId());
        }

        BoardResponseDto<List<Long>> boardResponseDto = new BoardResponseDto<>();
        boardResponseDto.setBoardResponse(introBoard, imageIds);
        return boardResponseDto;

    }

    public String deleteBoard(Long id) {
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

    public List<BoardResponseDto<String>> findAllBoard() {
        List<IntroBoard> boardList = introBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        List<BoardResponseDto<String>> boardResponseDtoList = new ArrayList<>();

        for(int i=0; i<=boardList.size()-1; i++) {
            boardResponseDtoList.add(boardList.get(i).toBoardResponseDto(boardList.get(i), thumbnailList.get(i).getLocation(request, thumbnailList.get(i))));
        }

        return boardResponseDtoList;
    }
}
