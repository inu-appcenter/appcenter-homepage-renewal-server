package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.FaqBoard;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.repository.BoardRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository<Board> boardRepository;

    @Transactional
    // (앱) 게시글 저장하기
    public CommonResponseDto saveBoard(BoardRequestDto boardRequestDto) {
        if (boardRequestDto instanceof IntroBoardRequestDto) {
            Board savedBoard = boardRepository.save(new IntroBoard((IntroBoardRequestDto) boardRequestDto));
            return new CommonResponseDto(savedBoard.getId() + " Board has been successfully saved.");
        } else if (boardRequestDto instanceof PhotoBoardRequestDto) {
            Board savedBoard = boardRepository.save(new PhotoBoard((PhotoBoardRequestDto) boardRequestDto));
            return new CommonResponseDto(savedBoard.getId() + " Board has been successfully saved.");
        } else if (boardRequestDto instanceof FaqBoardRequestDto) {
            Board savedBoard = boardRepository.save(new FaqBoard((FaqBoardRequestDto) boardRequestDto));
            return new CommonResponseDto(savedBoard.getId() + " Board has been successfully saved.");
        } else {
            throw new CustomNotFoundException("해당하는 타입이 없습니다.");
        }
    }

    @Transactional
    public CommonResponseDto deleteBoard(Long id) {
        boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 보드가 없습니다."));
        boardRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

}
