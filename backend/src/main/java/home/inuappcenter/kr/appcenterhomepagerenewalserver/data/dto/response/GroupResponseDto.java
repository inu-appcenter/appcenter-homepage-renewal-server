package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupResponseDto {
    private Long group_id;
    private String member;
    private String profileImage;
    private String email;
    private String blogLink;
    private String gitRepositoryLink;
    private String role;
    private String part;
    private Double year;

    public void setGroupResponseDto(Group group) {
        this.group_id = group.getGroup_id();
        this.member = group.getMember().getName();
        this.profileImage = group.getMember().getProfileImage();
        this.email = group.getMember().getEmail();
        this.blogLink = group.getMember().getBlogLink();
        this.gitRepositoryLink = group.getMember().getGitRepositoryLink();
        this.role = group.getRole().getRole_name();
        this.part = group.getPart();
        this.year = group.getYear();
    }
}
