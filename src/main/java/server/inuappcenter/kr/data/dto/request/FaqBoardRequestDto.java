package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
public class FaqBoardRequestDto {
    @Schema(
            example = "서버",
            description = "파트"
    )
    @NotNull
    private String part;

    @Schema(
            example = "질문입니다.",
            description = "질문"
    )
    @NotNull
    private String question;

    @Schema(
            example = "답변입니다.",
            description = "답변"
    )
    @NotNull
    private String answer;
}
