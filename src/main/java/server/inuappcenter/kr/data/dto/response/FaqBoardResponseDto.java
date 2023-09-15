package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

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
}
