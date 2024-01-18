package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.repository.PhotoBoardRepository;
import server.inuappcenter.kr.data.utils.BoardUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoBoardService {
    private final BoardService boardService;
    private final PhotoBoardRepository photoBoardRepository;
    private final HttpServletRequest request;

    @Transactional(readOnly = true)
    public PhotoBoardResponseDto getPhotoBoard(Long id) {
        Board foundBoard = boardService.getBoard(id);
        return PhotoBoardResponseDto.entityToDto(request, foundBoard);
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
