package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MemberRequestDto {
    @Schema(
            example = "홍길동",
            description = "이름"
    )
    @NotBlank(message = "동아리원의 이름이 비어있을 수 없습니다.")
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
            description = "Github 저장소 URL"
    )
    private String gitRepositoryLink;

    @Schema(
            example = "https://...",
            description = "Behance URL"
    )
    private String behanceLink;

    @Schema(
            example = "010-0000-0000",
            description = "전화번호"
    )
    private String phoneNumber;

    @Schema(
            example = "000000000",
            description = "학번"
    )
    @NotBlank(message = "학번이 비어있을 수 없습니다.")
    private String studentNumber;

    @Schema(
            example = "컴퓨터공학부",
            description = "학과"
    )
    private String department;
}
