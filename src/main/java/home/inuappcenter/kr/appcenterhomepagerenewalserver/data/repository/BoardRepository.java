package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository<T extends Board> extends JpaRepository<T, Long> {
}
