package server.inuappcenter.kr.data.redis.repository;

import org.springframework.data.repository.CrudRepository;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;

public interface BoardResponseRedisRepository<T extends BoardResponseDto> extends CrudRepository<T, Long> {
    BoardResponseDto getById(Long id);
}
