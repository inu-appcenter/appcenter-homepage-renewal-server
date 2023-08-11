package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.BoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.BoardResponseDto;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class IntroBoard extends Board{
    public String title;
    public String subTitle;
    public String androidStoreLink;
    public String iOSStoreLink;
    public String body;


    @OneToMany(mappedBy = "introBoard")
    private List<Image> Images = new ArrayList<>();

    public void setIntroBoard(BoardRequestDto boardRequestDto) {
        this.body = boardRequestDto.getBoard();
    }

    public BoardResponseDto<String> toBoardResponseDto(IntroBoard introBoard, String image) {
        BoardResponseDto<String> boardResponseDto = new BoardResponseDto<>();
        boardResponseDto.setBoardResponse(introBoard, image);
        return boardResponseDto;
    }
}
