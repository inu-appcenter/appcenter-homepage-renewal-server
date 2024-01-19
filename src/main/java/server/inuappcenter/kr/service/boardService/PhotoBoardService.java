package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.repository.PhotoBoardRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhotoBoardService {
    private final PhotoBoardRepository photoBoardRepository;
    private final HttpServletRequest request;

    @Transactional(readOnly = true)
    public List<BoardResponseDto> findPhotoBoardList() {
        List<BoardResponseDto> responseDtoList= new ArrayList<>();
        for (Board board : photoBoardRepository.findAll()) {
            responseDtoList.add(board.createResponse(request));
        }
        return responseDtoList;
    }



}
