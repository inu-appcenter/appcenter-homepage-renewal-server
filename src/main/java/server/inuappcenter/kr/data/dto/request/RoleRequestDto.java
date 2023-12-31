package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleRequestDto {
    @Schema(
            example = "파트장",
            description = "역할: 파트장, 파트원.. 중 하나"
    )
    @NotBlank(message = "파트장이 비어있을 수 없습니다.")
    private String role_name;
}
