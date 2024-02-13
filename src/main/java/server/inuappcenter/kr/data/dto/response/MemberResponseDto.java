package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.Member;

import java.time.LocalDateTime;

@Getter
public class MemberResponseDto {
    private final Long member_id;
    private final String name;
    private final String description;
    private final String profileImage;
    private final String blogLink;
    private final String email;
    private final String gitRepositoryLink;
    private final String behanceLink;
    private final String phoneNumber;
    private final String studentNumber;
    private final String department;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private MemberResponseDto(Long member_id, String name, String description, String profileImage, String blogLink, String email, String gitRepositoryLink,
                              LocalDateTime createdDate, LocalDateTime lastModifiedDate, String behanceLink, String phoneNumber, String studentNumber, String department) {
        this.member_id = member_id;
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.blogLink = blogLink;
        this.email = email;
        this.gitRepositoryLink = gitRepositoryLink;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.behanceLink = behanceLink;
        this.phoneNumber = phoneNumber;
        this.studentNumber = studentNumber;
        this.department = department;
    }

    public static MemberResponseDto entityToDto(Member member) {
        return MemberResponseDto.builder()
                .member_id(member.getId())
                .name(member.getName())
                .description(member.getDescription())
                .profileImage(member.getProfileImage())
                .blogLink(member.getBlogLink())
                .email(member.getEmail())
                .gitRepositoryLink(member.getGitRepositoryLink())
                .createdDate(member.getCreatedDate())
                .lastModifiedDate(member.getLastModifiedDate())
                .behanceLink(member.getBehanceLink())
                .phoneNumber(member.getPhoneNumber())
                .studentNumber(member.getStudentNumber())
                .department(member.getDepartment())
                .build();
    }
}
