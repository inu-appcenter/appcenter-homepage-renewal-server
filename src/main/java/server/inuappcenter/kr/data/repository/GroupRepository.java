package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.Role;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    ArrayList<Group> findAllByMember(Member member);
    ArrayList<Group> findAllByRole(Role role);

    List<Group> findAllByMember_Name(String name);

    @Query("SELECT DISTINCT e.year FROM Group e ORDER BY e.year")
    List<Double> findAllYears();

    @Query("SELECT e FROM Group e WHERE e.year = :year AND e.part = :part ORDER BY CASE WHEN e.role.roleName = '파트장' THEN 0 ELSE 1 END, e.member.name")
    List<Group> findAllByYearAndPartOrderByYear(Double year, String part);

    @Query("SELECT e FROM Group e WHERE e.year = :year ORDER BY CASE WHEN e.role.roleName = '파트장' THEN 0 ELSE 1 END, e.part")
    List<Group> findAllByYearOrderByPart(Double year);

    @Query("SELECT e FROM Group e WHERE e.part = :part ORDER BY CASE WHEN e.role.roleName = '파트장' THEN 0 ELSE 1 END, e.year DESC")
    List<Group> findAllByPartOrderByYearDesc(String part);

    @Query("SELECT DISTINCT e.part FROM Group e")
    List<String> findAllParts();

}
