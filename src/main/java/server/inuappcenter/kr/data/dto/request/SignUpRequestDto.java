package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignUpRequestDto {

    @Schema(example = "APPCENTER2025", description = "[필수] 회원가입 인증코드", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "인증코드가 비어있을 수 없습니다.")
    private String registrationCode;

    @Schema(example = "admin", description = "[필수] 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "아이디가 비어있을 수 없습니다.")
    private String uid;

    @Schema(example = "password123", description = "[필수] 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "비밀번호가 비어있을 수 없습니다.")
    private String password;

    @Schema(example = "홍길동", description = "[필수] 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "동아리원의 이름이 비어있을 수 없습니다.")
    private String name;

    @Schema(example = "안녕하세요 저는...", description = "[선택] 자기소개")
    private String description;

    @Schema(example = "https://...", description = "[선택] 프로필 이미지 URL")
    private String profileImage;

    @Schema(example = "test@inu.ac.kr", description = "[필수] 이메일 (기존 멤버 매칭에 사용)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이메일이 비어있을 수 없습니다.")
    private String email;

    @Schema(example = "https://...", description = "[선택] 블로그 URL")
    private String blogLink;

    @Schema(example = "https://...", description = "[선택] Github 저장소 URL")
    private String gitRepositoryLink;

    @Schema(example = "https://...", description = "[선택] Behance URL")
    private String behanceLink;

    @Schema(example = "010-0000-0000", description = "[필수] 전화번호 (기존 멤버 매칭에 사용)", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "전화번호가 비어있을 수 없습니다.")
    private String phoneNumber;

    @Schema(example = "000000000", description = "[필수] 학번", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "학번이 비어있을 수 없습니다.")
    private String studentNumber;

    @Schema(example = "컴퓨터공학부", description = "[선택] 학과")
    private String department;

    public MemberRequestDto toMemberRequestDto() {
        return new MemberRequestDto(
                name, description, profileImage, blogLink, email,
                gitRepositoryLink, behanceLink, phoneNumber, studentNumber, department
        );
    }
}
