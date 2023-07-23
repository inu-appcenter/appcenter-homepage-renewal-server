package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {
    @Schema(
            example = "홍길동",
            description = "이름"
    )
    private String name;

    @Schema(
            example = "안녕하세요 저는...",
            description = "자기소개"
    )
    private String description;

    @Schema(
            example = "https://...",
            description = "프로필 이미지 URL"
    )
    private String profileImage;

    @Schema(
            example = "https://...",
            description = "블로그 URL"
    )
    private String blogLink;

    @Schema(
            example = "test@inu.ac.kr",
            description = "이메일"
    )
    private String email;

    @Schema(
            example = "https://...",
            description = "Git 저장소 URL"
    )
    private String gitRepositoryLink;
}
