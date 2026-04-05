package server.inuappcenter.kr.data.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import server.inuappcenter.kr.data.domain.Group;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GroupResponseDto {
    private final Long group_id;
    private final String member;
    private final String profileImage;
    private final String email;
    private final String blogLink;
    private final String gitRepositoryLink;
    private final String role;
    private final String part;
    private final Double year;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;
    private final List<MemberProjectInfoDto> projects;


    @Jacksonized
    @Builder
    private GroupResponseDto(Long group_id, String member, String profileImage, String email,
                             String blogLink, String gitRepositoryLink, String role, String part, Double year,
                             LocalDateTime createdDate, LocalDateTime lastModifiedDate,
                             List<MemberProjectInfoDto> projects) {
        this.group_id = group_id;
        this.member = member;
        this.profileImage = profileImage;
        this.email = email;
        this.blogLink = blogLink;
        this.gitRepositoryLink = gitRepositoryLink;
        this.role = role;
        this.part = part;
        this.year = year;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
        this.projects = projects;
    }

    private static String normalizePart(String part) {
        if (part == null) return null;
        switch (part.toLowerCase()) {
            case "server": case "android": case "ios": case "web": return "Dev";
            default: return part;
        }
    }

    public static GroupResponseDto entityToDto(Group group) {
        return new GroupResponseDto.GroupResponseDtoBuilder()
                .group_id(group.getGroup_id())
                .member(group.getMember().getName())
                .profileImage(group.getMember().getProfileImage())
                .email(group.getMember().getEmail())
                .blogLink(group.getMember().getBlogLink())
                .gitRepositoryLink(group.getMember().getGitRepositoryLink())
                .role(group.getRole().getRoleName())
                .part(normalizePart(group.getPart()))
                .year(group.getYear())
                .createdDate(group.getCreatedDate())
                .lastModifiedDate(group.getLastModifiedDate())
                .build();
    }

    public static GroupResponseDto entityToDtoWithProjects(Group group, List<MemberProjectInfoDto> projects) {
        return new GroupResponseDto.GroupResponseDtoBuilder()
                .group_id(group.getGroup_id())
                .member(group.getMember().getName())
                .profileImage(group.getMember().getProfileImage())
                .email(group.getMember().getEmail())
                .blogLink(group.getMember().getBlogLink())
                .gitRepositoryLink(group.getMember().getGitRepositoryLink())
                .role(group.getRole().getRoleName())
                .part(normalizePart(group.getPart()))
                .year(group.getYear())
                .createdDate(group.getCreatedDate())
                .lastModifiedDate(group.getLastModifiedDate())
                .projects(projects)
                .build();
    }

    // 프로젝트 조회시 사용
    public static GroupResponseDto toIntroBoardDto(Group group) {
        return new GroupResponseDto.GroupResponseDtoBuilder()
                .group_id(group.getGroup_id())
                .member(group.getMember().getName())
                .profileImage(group.getMember().getProfileImage())
                .part(group.getPart())
                .build();
    }
}
