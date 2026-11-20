package server.inuappcenter.kr.data.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 이미지 원본(압축 해제된 바이트) Redis 캐시.
 *
 * 키 하나에 바이트를 그대로 저장한다(SET image:{id} <bytes> EX 3600).
 * 예전에는 @RedisHash(ImageRedis)를 썼는데, 저장할 때 Spring Data Redis가
 * byte[]를 원소 단위로 색인 검사(PathIndexResolver)해서 1MB 이미지 하나에
 * 약 2GB의 임시 객체가 생겼다. 단순 키-값은 이 과정이 없다.
 */
@Component
public class ImageCache {
    private static final String PREFIX = "image:";
    private static final Duration TTL = Duration.ofHours(1);

    private final RedisTemplate<String, byte[]> redis;

    public ImageCache(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, byte[]> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.byteArray());
        template.afterPropertiesSet();
        this.redis = template;
    }

    public Optional<byte[]> get(Long id) {
        return Optional.ofNullable(redis.opsForValue().get(key(id)));
    }

    public void put(Long id, byte[] data) {
        redis.opsForValue().set(key(id), data, TTL);
    }

    public void evict(Long id) {
        redis.delete(key(id));
    }

    public void evictAll(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<String> keys = ids.stream().map(ImageCache::key).collect(Collectors.toList());
        redis.delete(keys);
    }

    private static String key(Long id) {
        return PREFIX + id;
    }
}
