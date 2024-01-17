package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.inuappcenter.kr.data.domain.board.Board;

public interface BoardRepository<T extends Board> extends JpaRepository<T, Long> {
}
