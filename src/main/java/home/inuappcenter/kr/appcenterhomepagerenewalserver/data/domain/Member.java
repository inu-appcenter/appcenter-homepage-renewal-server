package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.MemberRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.MemberResponseDto;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long member_id;

    private String name;

    private String description;

    @Column(name = "profile_image")
    private String profileImage;

    private String email;

    @Column(name = "blog_link")
    private String blogLink;

    @Column(name = "git_repository_link")
    private String gitRepositoryLink;

    public void setMember(MemberRequestDto memberRequestDto) {
        this.name = memberRequestDto.getName();
        this.description = memberRequestDto.getDescription();
        this.profileImage = memberRequestDto.getProfileImage();
        this.email = memberRequestDto.getEmail();
        this.blogLink = memberRequestDto.getBlogLink();
        this.gitRepositoryLink = memberRequestDto.getGitRepositoryLink();
    }

    public void setMember(Long id, MemberRequestDto memberRequestDto) {
        this.member_id = id;
        this.name = memberRequestDto.getName();
        this.description = memberRequestDto.getDescription();
        this.profileImage = memberRequestDto.getProfileImage();
        this.email = memberRequestDto.getEmail();
        this.blogLink = memberRequestDto.getBlogLink();
        this.gitRepositoryLink = memberRequestDto.getGitRepositoryLink();
    }

    public MemberResponseDto toMemberResponseDto(Member member) {
        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setMemberResponseDto(member);
        return memberResponseDto;
    }
}
