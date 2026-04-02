package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;
import server.inuappcenter.kr.data.domain.Member;

import java.util.List;

@Getter
public class MemberWithGroupsResponseDto {
    private final Long memberId;
    private final String name;
    private final String description;
    private final String profileImage;
    private final String email;
    private final String blogLink;
    private final String gitRepositoryLink;
    private final String behanceLink;
    private final String department;
    private final List<MemberGroupEntryDto> groups;
    private final List<MemberProjectInfoDto> projects;

    public MemberWithGroupsResponseDto(Member member, List<MemberGroupEntryDto> groups, List<MemberProjectInfoDto> projects) {
        this.memberId = member.getId();
        this.name = member.getName();
        this.description = member.getDescription();
        this.profileImage = member.getProfileImage();
        this.email = member.getEmail();
        this.blogLink = member.getBlogLink();
        this.gitRepositoryLink = member.getGitRepositoryLink();
        this.behanceLink = member.getBehanceLink();
        this.department = member.getDepartment();
        this.groups = groups;
        this.projects = projects;
    }
}
