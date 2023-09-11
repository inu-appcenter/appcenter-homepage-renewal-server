package server.inuappcenter.kr.data.domain.board;

import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;

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

    public FaqBoardResponseDto toResponseDto(FaqBoard faqBoard) {
       return new FaqBoardResponseDto(
               faqBoard.getId(),
               faqBoard.getPart(),
               faqBoard.getQuestion(),
               faqBoard.getAnswer()
       );
    }

}
