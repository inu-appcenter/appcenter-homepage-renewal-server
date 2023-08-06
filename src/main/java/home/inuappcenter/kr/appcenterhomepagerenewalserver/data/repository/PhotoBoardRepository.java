package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.PhotoBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoBoardRepository extends JpaRepository<PhotoBoard, Long> {
}
