package server.inuappcenter.kr.service.boardService.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.repository.IntroBoardRepository;
import server.inuappcenter.kr.service.boardService.AdditionalBoardService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service("IntroBoardServiceImpl")
@RequiredArgsConstructor
public class IntroBoardServiceImpl implements AdditionalBoardService {
    private final IntroBoardRepository introBoardRepository;
    private final HttpServletRequest request;

    @Override
    @Transactional(readOnly = true)
    public List<BoardResponseDto> findBoardList() {
        List<BoardResponseDto> responseDtoList= new ArrayList<>();
        for (Board board : introBoardRepository.findAll()) {
            responseDtoList.add(board.createResponse(request));
        }
        return responseDtoList;
    }


}
