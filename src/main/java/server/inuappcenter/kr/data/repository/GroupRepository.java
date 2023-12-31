package server.inuappcenter.kr.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.Role;

import java.util.ArrayList;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    ArrayList<Group> findAllByMember(Member member);
    ArrayList<Group> findAllByRole(Role role);

}
