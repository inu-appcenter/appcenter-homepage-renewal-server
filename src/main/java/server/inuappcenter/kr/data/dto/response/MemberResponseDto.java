package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponseDto {
    private final Long member_id;
    private final String name;
    private final String description;
    private final String profileImage;
    private final String blogLink;
    private final String email;
    private final String gitRepositoryLink;

    @Builder
    private MemberResponseDto(Long member_id, String name, String description, String profileImage, String blogLink, String email, String gitRepositoryLink) {
        this.member_id = member_id;
        this.name = name;
        this.description = description;
        this.profileImage = profileImage;
        this.blogLink = blogLink;
        this.email = email;
        this.gitRepositoryLink = gitRepositoryLink;
    }
}
