package server.inuappcenter.kr.data.redis.repository;

import org.springframework.data.repository.CrudRepository;
import server.inuappcenter.kr.data.redis.domain.ImageRedis;

public interface ImageRedisRepository extends CrudRepository<ImageRedis, Long> {
}
