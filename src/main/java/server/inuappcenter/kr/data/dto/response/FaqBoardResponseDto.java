package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.FaqBoard;

import java.time.LocalDateTime;

@Getter
public class FaqBoardResponseDto extends BoardResponseDto{
    private final String part;
    private final String question;
    private final String answer;


    public FaqBoardResponseDto(Long id, String part, String question, String answer, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.part = part;
        this.question = question;
        this.answer = answer;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static FaqBoardResponseDto entityToDto(Board board) {
        FaqBoard faqBoard = createBoardResponse(board);
        return new FaqBoardResponseDto(
                faqBoard.getId(),
                faqBoard.getPart(),
                faqBoard.getQuestion(),
                faqBoard.getAnswer(),
                faqBoard.getCreatedDate(),
                faqBoard.getLastModifiedDate()
        );
    }

    public static FaqBoard createBoardResponse(Board board) {
        return (FaqBoard) board;
    }
}
