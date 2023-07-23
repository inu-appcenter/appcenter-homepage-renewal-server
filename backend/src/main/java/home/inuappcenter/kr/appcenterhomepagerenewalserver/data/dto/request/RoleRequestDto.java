package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleRequestDto {
    @Schema(
            example = "파트장",
            description = "역할: 파트장, 파트원.. 중 하나"
    )
    private String role_name;
}
