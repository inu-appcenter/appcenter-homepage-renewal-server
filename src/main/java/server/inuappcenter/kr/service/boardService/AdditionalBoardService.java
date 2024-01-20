package server.inuappcenter.kr.service.boardService;

import server.inuappcenter.kr.data.dto.response.BoardResponseDto;

import java.util.List;

public interface AdditionalBoardService {
    List<BoardResponseDto> findBoardList();
}
