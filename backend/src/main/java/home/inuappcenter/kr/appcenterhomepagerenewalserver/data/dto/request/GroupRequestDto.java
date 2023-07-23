package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GroupRequestDto {
    @Schema(
            example = "서버",
            description = "파트 중 하나"
    )
    @NotBlank
    private String part;

    @Schema(
            example = "14",
            description = "기수"
    )
    @NotBlank
    @Min(value = 13)
    private Double year;
}
