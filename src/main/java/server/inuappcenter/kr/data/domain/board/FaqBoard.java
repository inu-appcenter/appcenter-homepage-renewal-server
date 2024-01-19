package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FaqBoard extends Board {

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

    @Override
    public void modifyBoard(BoardRequestDto boardRequestDto) {
        this.updateFaqBoard((FaqBoardRequestDto) boardRequestDto);
    }

    @Override
    public void updateImage(List<Image> image) {
    }

    @Override
    public BoardResponseDto createResponse(HttpServletRequest request) {
        return new FaqBoardResponseDto(
                this.getId(), this.part, this.question, this.answer, this.getCreatedDate(), this.getLastModifiedDate()
        );
    }
}
