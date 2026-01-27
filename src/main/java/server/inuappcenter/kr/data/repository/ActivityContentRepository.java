package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.board.ActivityBoard;
import server.inuappcenter.kr.data.domain.board.ActivityContent;

import java.util.List;

@Repository
public interface ActivityContentRepository extends JpaRepository<ActivityContent, Long> {
    List<ActivityContent> findAllByActivityBoardOrderBySequenceAsc(ActivityBoard activityBoard);
}