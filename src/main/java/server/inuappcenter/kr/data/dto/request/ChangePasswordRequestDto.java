package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangePasswordRequestDto {

    @Schema(example = "currentPassword123", description = "[필수] 현재 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "현재 비밀번호가 비어있을 수 없습니다.")
    private String currentPassword;

    @Schema(example = "newPassword456", description = "[필수] 새 비밀번호", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "새 비밀번호가 비어있을 수 없습니다.")
    private String newPassword;
}