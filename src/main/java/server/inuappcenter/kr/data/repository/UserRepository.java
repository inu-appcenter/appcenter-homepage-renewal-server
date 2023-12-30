package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.inuappcenter.kr.data.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByUid(String uid);
}
