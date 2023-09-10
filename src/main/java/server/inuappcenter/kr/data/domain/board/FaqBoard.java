package server.inuappcenter.kr.data.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;

import javax.persistence.Entity;

@Getter
@Entity
@NoArgsConstructor
public class FaqBoard extends Board{

    private String part;
    private String question;
    private String answer;

    public FaqBoard(FaqBoardRequestDto faqBoardRequestDto) {
        this.part = faqBoardRequestDto.getPart();
        this.question = faqBoardRequestDto.getQuestion();
        this.answer = faqBoardRequestDto.getAnswer();
    }

    public void updateFaqBoard(FaqBoardRequestDto faqBoardRequestDto) {
        this.part = faqBoardRequestDto.getPart();
        this.question = faqBoardRequestDto.getQuestion();
        this.answer = faqBoardRequestDto.getAnswer();
    }

}
