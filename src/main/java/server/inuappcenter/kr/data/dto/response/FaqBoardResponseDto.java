package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.FaqBoard;

import java.time.LocalDateTime;

@Getter
public class FaqBoardResponseDto {
    private final Long id;
    private final String part;
    private final String question;
    private final String answer;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    public FaqBoardResponseDto(Long id, String part, String question, String answer, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.part = part;
        this.question = question;
        this.answer = answer;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static FaqBoardResponseDto entityToDto(FaqBoard faqBoard) {
        return new FaqBoardResponseDto(
                faqBoard.getId(),
                faqBoard.getPart(),
                faqBoard.getQuestion(),
                faqBoard.getAnswer(),
                faqBoard.getCreatedDate(),
                faqBoard.getLastModifiedDate()
        );
    }
}
