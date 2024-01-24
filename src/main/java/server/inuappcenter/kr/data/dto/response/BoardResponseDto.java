package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Getter
@RedisHash(value = "board")
public abstract class BoardResponseDto {
    @Id
    protected Long id;
    protected String body;
    protected LocalDateTime createdDate;
    protected LocalDateTime lastModifiedDate;
}
