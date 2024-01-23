package server.inuappcenter.kr.data.repository.redis;

import org.springframework.data.repository.CrudRepository;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;

public interface BoardRedisRepository extends CrudRepository<BoardResponseDto, Long> {
}
