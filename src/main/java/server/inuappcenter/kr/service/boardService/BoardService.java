package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.repository.BoardRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository<Board> boardRepository;

    @Transactional
    public Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("해당되는 Board를 찾을 수 없습니다."));
    }

    @Transactional
    public CommonResponseDto saveBoard(BoardRequestDto boardRequestDto) {
        Board savedBoard = boardRepository.save(boardRequestDto.createBoard());
        return new CommonResponseDto(savedBoard.getId() + " Board has been successfully saved.");
    }

    @Transactional
    public CommonResponseDto deleteBoard(Long id) {
        boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 보드가 없습니다."));
        boardRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

}
