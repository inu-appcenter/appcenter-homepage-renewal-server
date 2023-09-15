package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PhotoBoardResponseDto<T> {
    private final Long board_id;
    private final String body;
    private final T images;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    public PhotoBoardResponseDto (Long board_id, String body, T images, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.board_id = board_id;
        this.body = body;
        this.images = images;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
