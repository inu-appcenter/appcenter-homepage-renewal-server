package server.inuappcenter.kr.service.boardService;

import server.inuappcenter.kr.data.dto.response.BoardResponseDto;

import java.util.List;

public interface AdditionalBoardStrategyProvider {
    List<BoardResponseDto> findBoardList(String boardName, String topic);
}
