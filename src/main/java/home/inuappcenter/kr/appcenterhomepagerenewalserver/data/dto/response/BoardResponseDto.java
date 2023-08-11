package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.PhotoBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardResponseDto<T> {
    private Long board_id;
    private String body;
    private T images;

    public void setBoardResponse(IntroBoard introBoard, T images) {
        this.board_id = introBoard.getId();
        this.body = introBoard.getBody();
        this.images = images;
    }

    public void setBoardResponse(PhotoBoard photoBoard, T images) {
        this.board_id = photoBoard.getId();
        this.body = photoBoard.getBody();
        this.images = images;
    }
}
