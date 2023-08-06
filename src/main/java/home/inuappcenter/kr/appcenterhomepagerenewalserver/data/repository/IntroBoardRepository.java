package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntroBoardRepository extends JpaRepository<IntroBoard, Long> {
}
