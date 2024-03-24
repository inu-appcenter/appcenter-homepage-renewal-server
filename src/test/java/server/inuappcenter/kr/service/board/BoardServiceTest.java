package server.inuappcenter.kr.service.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.FaqBoard;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
import server.inuappcenter.kr.data.redis.repository.BoardResponseRedisRepository;
import server.inuappcenter.kr.data.repository.BoardRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;
import server.inuappcenter.kr.service.boardService.BoardService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {
    @Mock
    private BoardRepository<Board> boardRepository;
    @Mock
    private BoardResponseRedisRepository<BoardResponseDto> boardResponseRedisRepository;

    @InjectMocks
    private BoardService boardService;

    @DisplayName("게시판 가져오기 테스트")
    @Test
    public void getBoard() {
        // given
        Long givenId = 1L;
        Board exceptedEntity = new FaqBoard(new FaqBoardRequestDto(
                "서버", "질문입니다.", "답변입니다."
        ));
        BoardResponseDto responseDto = FaqBoardResponseDto.entityToDto(exceptedEntity);
        given(boardResponseRedisRepository.findById(givenId)).willReturn(Optional.of(responseDto));
        // when
        BoardResponseDto result = boardService.findBoard(givenId);
        // then
        assertEquals(exceptedEntity.getId(), result.getId());
        assertEquals(exceptedEntity.getBody(), result.getBody());
    }

    @DisplayName("게시판 저장 테스트")
    @Test
    public void saveBoardTest() {
        // given
        FaqBoardRequestDto givenDto = new FaqBoardRequestDto(
                "서버", "질문입니다.", "답변입니다."
        );
        FaqBoard expectedEntity = new FaqBoard(givenDto);
        given(boardRepository.save(Mockito.any(FaqBoard.class))).willReturn(expectedEntity);
        CommonResponseDto exceptedResult = new CommonResponseDto(expectedEntity.getId() + " Board has been successfully saved.");
        // when
        CommonResponseDto result = boardService.saveBoard(givenDto);
        // then
        assertEquals(exceptedResult.getMsg(), result.getMsg());
    }

    @DisplayName("게시판 삭제 테스트")
    @Test
    public void deleteBoardTest() {
        // given
        Long givenId = 1L;
        Board exceptedEntity = new FaqBoard(new FaqBoardRequestDto(
                "서버", "질문입니다.", "답변입니다."
        ));
        given(boardRepository.findById(givenId)).willReturn(Optional.of(exceptedEntity));
        // when
        CommonResponseDto result = boardService.deleteBoard(givenId);
        // then
        assertEquals("id: " + givenId + " has been successfully deleted.", result.getMsg());

        // case2 (게시판이 존재하지 않을 때)
        // given
        given(boardRepository.findById(givenId)).willReturn(Optional.empty());
        // when, then
        assertThrows(CustomNotFoundException.class, () -> boardService.deleteBoard(givenId));
    }

}
