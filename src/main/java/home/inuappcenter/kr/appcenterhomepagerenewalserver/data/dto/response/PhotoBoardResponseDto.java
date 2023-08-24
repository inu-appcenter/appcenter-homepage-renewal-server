package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PhotoBoardResponseDto<T> {
    private final Long board_id;
    private final String body;
    private final T images;

    @Builder
    private PhotoBoardResponseDto (Long board_id, String body, T images) {
        this.board_id = board_id;
        this.body = body;
        this.images = images;
    }
}
