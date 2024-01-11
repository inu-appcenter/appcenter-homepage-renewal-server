package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.repository.IntroBoardRepository;
import server.inuappcenter.kr.data.utils.BoardUtils;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntroBoardService {
    private final BoardService boardService;
    private final IntroBoardRepository introBoardRepository;
    private final ImageRepository imageRepository;
    private final HttpServletRequest request;


    @Transactional(readOnly = true)
    // (앱) 게시글 조회하기
    public IntroBoardResponseDto getIntroBoard(Long id) {
        Board foundBoard = boardService.getBoard(id);
        return IntroBoardResponseDto.entityToDto(request, foundBoard);
    }

    @Transactional
    public IntroBoardResponseDto updateIntroBoard(IntroBoardRequestDto introBoardRequestDto, Long board_id) {
        IntroBoard foundBoard = introBoardRepository.findById(board_id).orElseThrow(()-> new CustomNotFoundException("The requested ID was not found."));
        foundBoard.updateBoard(introBoardRequestDto);
        List<Image> foundImg = imageRepository.findByIntroBoard(foundBoard);

        for(Image image: foundImg) {
            for(MultipartFile multipartFile: introBoardRequestDto.getMultipartFiles()) {
                image.setImage(multipartFile);
            }
        }

        IntroBoard introBoard = introBoardRepository.save(foundBoard);
        List<Image> savedImage = imageRepository.saveAll(foundImg);

        return IntroBoardResponseDto.entityToDto(request, introBoard);
    }

    @Transactional(readOnly = true)
    public List<IntroBoardResponseDto> findAllIntroBoard() {
        List<IntroBoard> boardList = introBoardRepository.findAll();
        List<Image> thumbnailList = new ArrayList<>();
        for (IntroBoard introBoard : boardList) {
            for (int j = 0; j < introBoard.getImages().size(); j++) {
                if (introBoard.getImages().get(j).getIsThumbnail()) {
                    thumbnailList.add(introBoard.getImages().get(j));
                }
            }
        }
        return BoardUtils.returnIntroBoardResponseDtoList(boardList, thumbnailList, request);
    }


}
