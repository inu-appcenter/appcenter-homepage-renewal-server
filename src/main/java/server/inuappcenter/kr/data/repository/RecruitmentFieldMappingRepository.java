package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.RecruitmentFieldMapping;
import server.inuappcenter.kr.data.domain.board.Recruitment;

import java.util.List;

@Repository
public interface RecruitmentFieldMappingRepository extends JpaRepository<RecruitmentFieldMapping, Long> {
    List<RecruitmentFieldMapping> findAllByRecruitment(Recruitment recruitment);
    void deleteAllByRecruitment(Recruitment recruitment);
}
