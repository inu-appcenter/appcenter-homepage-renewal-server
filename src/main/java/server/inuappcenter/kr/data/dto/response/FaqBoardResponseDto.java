package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

@Getter
public class FaqBoardResponseDto {
    private final Long id;
    private final String part;
    private final String question;
    private final String answer;

    public FaqBoardResponseDto(Long id, String part, String question, String answer) {
        this.id = id;
        this.part = part;
        this.question = question;
        this.answer = answer;
    }
}
