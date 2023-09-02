package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.GroupRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.GroupResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "appcenter_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long group_id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private String part;

    private Double year;

    public Group(Member member, Role role, GroupRequestDto groupRequestDto) {
        this.member = member;
        this.role = role;
        this.part = groupRequestDto.getPart();
        this.year = groupRequestDto.getYear();
    }

    public void setGroup(Long group_id, GroupRequestDto groupRequestDto) {
        this.group_id = group_id;
        this.part = groupRequestDto.getPart();
        this.year = groupRequestDto.getYear();
    }

    public GroupResponseDto toGroupResponseDto(Group group) {
        return GroupResponseDto.builder()
                .group_id(group.getGroup_id())
                .member(group.getMember().getName())
                .profileImage(group.getMember().getProfileImage())
                .email(group.getMember().getEmail())
                .blogLink(group.getMember().getBlogLink())
                .gitRepositoryLink(group.getMember().getGitRepositoryLink())
                .role(group.getRole().getRole_name())
                .part(group.getPart())
                .year(group.getYear())
                .build();
    }
}
