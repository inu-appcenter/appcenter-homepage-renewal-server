package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;

public interface PhotoBoardRepository extends JpaRepository<PhotoBoard, Long> {
}
