package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegistrationCodeRequestDto {

    @Schema(example = "APPCENTER2025", description = "회원가입 인증코드")
    @NotBlank(message = "인증코드가 비어있을 수 없습니다.")
    private String code;
}
