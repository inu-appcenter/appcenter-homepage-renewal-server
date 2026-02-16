package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.IntroBoardGroup;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.board.IntroBoard;

import java.util.List;

@Repository
public interface IntroBoardGroupRepository extends JpaRepository<IntroBoardGroup, Long> {
    List<IntroBoardGroup> findAllByIntroBoard(IntroBoard introBoard);
    void deleteAllByIntroBoard(IntroBoard introBoard);
    List<IntroBoardGroup> findAllByGroup_Member(Member member);
}