package server.inuappcenter.kr.data.repository;

import server.inuappcenter.kr.data.domain.board.IntroBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroBoardRepository extends JpaRepository<IntroBoard, Long> {
}
