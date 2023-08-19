package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class IntroBoardRequestDto {
    @Schema(
            example = "Test Application",
            description = "앱 제목"
    )
    @NotBlank
    public String title;

    @Schema(
            example = "Example Todo-list Application",
            description = "부 제목"
    )
    @NotBlank
    public String subTitle;

    @Schema(
            example = "https://...",
            description = "플레이 스토어 링크"
    )
    @NotBlank
    public String androidStoreLink;

    @Schema(
            example = "https://...",
            description = "앱 스토어 링크"
    )
    @NotBlank
    public String iOSStoreLink;

    @Schema(
            example = "This Application is...",
            description = "앱 소개"
    )
    @NotBlank
    public String body;
}
