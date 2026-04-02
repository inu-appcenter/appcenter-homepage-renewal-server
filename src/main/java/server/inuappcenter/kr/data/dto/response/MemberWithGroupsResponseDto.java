package server.inuappcenter.kr.data.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonCreator
    public MemberWithGroupsResponseDto(
            @JsonProperty("memberId") Long memberId,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("profileImage") String profileImage,
            @JsonProperty("email") String email,
            @JsonProperty("blogLink") String blogLink,
            @JsonProperty("gitRepositoryLink") String gitRepositoryLink,
            @JsonProperty("behanceLink") String behanceLink,
            @JsonProperty("department") String department,
            @JsonProperty("groups") List<MemberGroupEntryDto> groups,
            @JsonProperty("projects") List<MemberProjectInfoDto> projects
    ) {
        this.memberId = memberId;
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.email = email;
        this.blogLink = blogLink;
        this.gitRepositoryLink = gitRepositoryLink;
        this.behanceLink = behanceLink;
        this.department = department;
        this.groups = groups;
        this.projects = projects;
    }
}
