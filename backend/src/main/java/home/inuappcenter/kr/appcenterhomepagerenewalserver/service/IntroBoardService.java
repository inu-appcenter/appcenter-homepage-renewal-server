package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.ImageRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.BoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.BoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.ImageRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.IntroBoardRepository;
import javax.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class IntroBoardService {
    private final IntroBoardRepository introBoardRepository;
    private final ImageRepository imageRepository;
    private final HttpServletRequest request;

    // 게시글 조회하기
    public BoardResponseDto<List<String>> getBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow();

        List<Image> ImageList = foundBoard.getImages();

        List<String> images = new ArrayList<>();

        for(Image image: ImageList) {
            images.add(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  "/image/photo/" + image.getId().toString());
        }

        BoardResponseDto<List<String>> boardResponseDto = new BoardResponseDto<>();
        boardResponseDto.setBoardResponse(foundBoard, images);
        return boardResponseDto;
    }


    // 게시글 저장하기
    public BoardResponseDto<List<Long>> saveBoard(BoardRequestDto boardRequestDto, ImageRequestDto imageRequestDto) throws IOException {
        IntroBoard introBoard = new IntroBoard();
        // introBoardRequestDto를 introBoard 타입으로 변환
        introBoard.setIntroBoard(boardRequestDto);

        // imageRequestDto를 List<Image> 타입으로 변환 / 게시판 정보도 함께 포함해서 저장시킴
        List<Image> imageList = new Image().toList(imageRequestDto, introBoard);

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

    // 게시글 수정
    public BoardResponseDto<List<Long>> updateBoard(BoardRequestDto boardRequestDto, Long board_id) throws Exception {
        // 보드 찾아오기
        IntroBoard foundBoard = introBoardRepository.findById(board_id).orElseThrow(Exception::new);

        foundBoard.setIntroBoard(boardRequestDto);

        // 찾은 보드에 새로운 정보를 기입해줌
        foundBoard.setIntroBoard(boardRequestDto);
        // introBoard를 저장
        IntroBoard introBoard = introBoardRepository.save(foundBoard);

        // 이미지 묶음 찾아오기 1. 게시판에 등록된 외래키 가져와서 2. 외래키로 imageRepository에서 find
        List<Image> foundImageList = imageRepository.findAllByIntroBoard(foundBoard);

        List<Long> imageIds = new ArrayList<>();

        for(Image image: foundImageList) {
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
