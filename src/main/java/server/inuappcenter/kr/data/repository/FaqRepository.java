package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.board.FaqBoard;

@Repository
public interface FaqRepository extends JpaRepository<FaqBoard, Long> {
}
