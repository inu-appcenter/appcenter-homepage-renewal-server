package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.EmailSubscription;

@Repository
public interface EmailSubscriptionRepository extends JpaRepository<EmailSubscription, Long> {
    boolean existsByEmail(String email);
}
