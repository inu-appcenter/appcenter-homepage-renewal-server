package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardRequestDto {
    @Schema(
            example = "안녕하세요",
            description = "본문"
    )
    @NotBlank
    String board;
}
