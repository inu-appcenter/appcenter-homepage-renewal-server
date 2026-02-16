package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByUid(String uid);
    Optional<User> findByUid(String uid);
    Optional<User> findByMember(Member member);
}
