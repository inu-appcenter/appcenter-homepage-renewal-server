package server.inuappcenter.kr.data.repository;

import server.inuappcenter.kr.data.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository<T extends Board> extends JpaRepository<T, Long> {
}
