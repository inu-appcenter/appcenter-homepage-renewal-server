package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResetPasswordRequestDto {

    @Schema(example = "admin", description = "[필수] 아이디", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "아이디가 비어있을 수 없습니다.")
    private String uid;

    @Schema(example = "홍길동", description = "[필수] 이름", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "이름이 비어있을 수 없습니다.")
    private String name;

    @Schema(example = "test@inu.ac.kr", description = "[선택] 이메일 (이메일/전화번호/학번 중 하나 필수)")
    private String email;

    @Schema(example = "010-0000-0000", description = "[선택] 전화번호 (이메일/전화번호/학번 중 하나 필수)")
    private String phoneNumber;

    @Schema(example = "000000000", description = "[선택] 학번 (이메일/전화번호/학번 중 하나 필수)")
    private String studentNumber;

    @Schema(example = "newPassword123", description = "[필수] 새 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "새 비밀번호가 비어있을 수 없습니다.")
    private String newPassword;
}