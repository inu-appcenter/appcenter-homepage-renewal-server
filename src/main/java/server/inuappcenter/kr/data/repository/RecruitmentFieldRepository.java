package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.RecruitmentField;

import java.util.Optional;

@Repository
public interface RecruitmentFieldRepository extends JpaRepository<RecruitmentField, Long> {
    Optional<RecruitmentField> findByName(String name);
    boolean existsByName(String name);
}
