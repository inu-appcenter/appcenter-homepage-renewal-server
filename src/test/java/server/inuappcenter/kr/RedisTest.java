package server.inuappcenter.kr;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
import server.inuappcenter.kr.data.repository.redis.BoardRedisRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class RedisTest {
    @Autowired
    private BoardRedisRepository boardRedisRepository;

    @Test
    void save() {
        BoardResponseDto boardResponseDto = new FaqBoardResponseDto(
                1L, "서버", "질문입니다.", "대답입니다.", LocalDateTime.now(), LocalDateTime.now()
        );
        boardRedisRepository.save(boardResponseDto);

        BoardResponseDto findResponse = boardRedisRepository.findById(boardResponseDto.getId()).orElseThrow(() -> new CustomNotFoundException("ID가 없습니다."));
        assertEquals(boardResponseDto.getId(), findResponse.getId());
    }
}
