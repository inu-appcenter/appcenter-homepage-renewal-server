package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Group;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Member;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group getByMember(Member member);
    Group getByRole(Role role);
}
