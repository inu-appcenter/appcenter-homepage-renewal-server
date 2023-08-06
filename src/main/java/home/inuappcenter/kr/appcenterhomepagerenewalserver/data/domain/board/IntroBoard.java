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
@Table(name = "introduction_board")
public class IntroBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long introduction_board_id;

    private String body;

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
