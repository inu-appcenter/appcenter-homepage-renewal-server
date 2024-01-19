package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BoardResponseDto {
    protected Long id;
    protected String body;
    protected LocalDateTime createdDate;
    protected LocalDateTime lastModifiedDate;
}
