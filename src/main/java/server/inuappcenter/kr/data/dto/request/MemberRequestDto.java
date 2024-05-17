package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;

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
    @Null
    @Pattern(regexp = "^https?://.*$", message = "profileImage의 URL 형식이 올바르지 않습니다.")
    private String profileImage;

    @Schema(
            example = "https://...",
            description = "블로그 URL"
    )
    @Null
    @Pattern(regexp = "^https?://.*$", message = "blogLink의 URL 형식이 올바르지 않습니다.")
    private String blogLink;

    @Schema(
            example = "test@inu.ac.kr",
            description = "이메일"
    )
    @Email
    private String email;

    @Schema(
            example = "https://...",
            description = "Github 저장소 URL"
    )
    @Null
    @Pattern(regexp = "^https?://.*$", message = "gitRepositoryLink의 URL 형식이 올바르지 않습니다.")
    private String gitRepositoryLink;

    @Schema(
            example = "https://...",
            description = "Behance URL"
    )
    @Null
    @Pattern(regexp = "^https?://.*$", message = "Behance의 URL 형식이 올바르지 않습니다.")
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
    private String studentNumber;

    @Schema(
            example = "컴퓨터공학부",
            description = "학과"
    )
    private String department;
}
