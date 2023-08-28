package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class GroupRequestDto {

    @Schema(
            example = "서버",
            description = "파트 중 하나"
    )
    @NotNull(message = "Part가 비어있을 수 없습니다.")
    private String part;

    @Schema(
            example = "14",
            description = "기수"
    )
    @NotNull(message = "Year가 비어있을 수 없습니다.")
    private Double year;
}
