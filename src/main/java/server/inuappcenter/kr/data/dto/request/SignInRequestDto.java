package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInRequestDto {
    @Schema(
            example = "id",
            description = "아이디"
    )
    @NotBlank
    private String id;

    @Schema(
            example = "password",
            description = "비밀번호"
    )
    @NotBlank
    private String password;


}
