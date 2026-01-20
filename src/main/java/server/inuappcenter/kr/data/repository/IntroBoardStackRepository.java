package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.IntroBoardStack;
import server.inuappcenter.kr.data.domain.board.IntroBoard;

import java.util.List;

@Repository
public interface IntroBoardStackRepository extends JpaRepository<IntroBoardStack, Long> {
    List<IntroBoardStack> findAllByIntroBoard(IntroBoard introBoard);
    void deleteAllByIntroBoard(IntroBoard introBoard);
}