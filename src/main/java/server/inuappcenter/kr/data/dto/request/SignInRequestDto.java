package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SignInRequestDto {
    @Schema(
            example = "id",
            description = "아이디"
    )
    @NotNull
    private String id;

    @Schema(
            example = "password",
            description = "비밀번호"
    )
    @NotNull
    private String password;

}
