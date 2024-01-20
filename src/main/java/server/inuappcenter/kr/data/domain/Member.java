package server.inuappcenter.kr.data.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;
import server.inuappcenter.kr.data.dto.request.MemberRequestDto;
import server.inuappcenter.kr.data.dto.response.MemberResponseDto;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "profile_image")
    private String profileImage;

    private String email;

    @Column(name = "blog_link")
    private String blogLink;

    @Column(name = "git_repository_link")
    private String gitRepositoryLink;

    public Member (MemberRequestDto memberRequestDto) {
        this.name = memberRequestDto.getName();
        this.description = memberRequestDto.getDescription();
        this.profileImage = memberRequestDto.getProfileImage();
        this.email = memberRequestDto.getEmail();
        this.blogLink = memberRequestDto.getBlogLink();
        this.gitRepositoryLink = memberRequestDto.getGitRepositoryLink();
    }

    public void updateMember(Long id, MemberRequestDto memberRequestDto) {
        this.id = id;
        this.name = memberRequestDto.getName();
        this.description = memberRequestDto.getDescription();
        this.profileImage = memberRequestDto.getProfileImage();
        this.email = memberRequestDto.getEmail();
        this.blogLink = memberRequestDto.getBlogLink();
        this.gitRepositoryLink = memberRequestDto.getGitRepositoryLink();
    }

    public MemberResponseDto toMemberResponseDto(Member member) {
        return MemberResponseDto.builder()
                .member_id(member.getId())
                .name(member.getName())
                .description(member.getDescription())
                .profileImage(member.getProfileImage())
                .blogLink(member.getBlogLink())
                .email(member.getEmail())
                .gitRepositoryLink(member.getGitRepositoryLink())
                .lastModifiedDate(member.getLastModifiedDate())
                .createdDate(member.getCreatedDate())
                .build();
    }
}
