package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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


    @Builder
    private GroupResponseDto(Long group_id, String member, String profileImage, String email,
                             String blogLink, String gitRepositoryLink, String role, String part, Double year,
                             LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
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
    }
}
