package server.inuappcenter.kr.data.redis.domain;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@Getter
@RedisHash(value = "image")
public class ImageRedis {
    @Id
    private Long id;
    private final byte[] imageData;

    public ImageRedis(Long id, byte[] imageData) {
        this.id = id;
        this.imageData = imageData;
    }
}
