package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findAllByName(String name);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByStudentNumber(String studentNumber);

    @Query("SELECT m FROM Member m WHERE REPLACE(m.phoneNumber, '-', '') = :phoneNumber")
    Optional<Member> findByPhoneNumberIgnoreDashes(@Param("phoneNumber") String phoneNumber);
}
