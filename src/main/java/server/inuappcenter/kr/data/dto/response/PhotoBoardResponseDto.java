package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

@Getter
public class PhotoBoardResponseDto<T> {
    private final Long board_id;
    private final String body;
    private final T images;

    public PhotoBoardResponseDto (Long board_id, String body, T images) {
        this.board_id = board_id;
        this.body = body;
        this.images = images;
    }
}
