package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto {
    private Long member_id;
    private String name;
    private String description;
    private String profileImage;
    private String blogLink;
    private String email;
    private String gitRepositoryLink;

    public void setMemberResponseDto(Member member) {
        this.member_id = member.getMember_id();
        this.name = member.getName();
        this.description = member.getDescription();
        this.profileImage = member.getProfileImage();
        this.blogLink = member.getBlogLink();
        this.email = member.getEmail();
        this.gitRepositoryLink = member.getGitRepositoryLink();
    }
}
