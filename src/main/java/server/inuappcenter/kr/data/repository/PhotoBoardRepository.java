package server.inuappcenter.kr.data.repository;

import server.inuappcenter.kr.data.domain.board.PhotoBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoBoardRepository extends JpaRepository<PhotoBoard, Long> {
}
