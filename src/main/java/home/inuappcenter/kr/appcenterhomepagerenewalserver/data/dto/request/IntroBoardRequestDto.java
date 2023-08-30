package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@ToString
public class IntroBoardRequestDto {
    @Schema(
            example = "Test Application",
            description = "앱 제목"
    )
    @NotBlank(message = "앱 제목이 비어있을 수 없습니다.")
    public String title;

    @Schema(
            example = "Example Todo-list Application",
            description = "부 제목"
    )
    @NotBlank(message = "부 제목이 비어있을 수 없습니다.")
    public String subTitle;

    @Schema(
            example = "https://...",
            description = "플레이 스토어 링크"
    )
    public String androidStoreLink;

    @Schema(
            example = "https://...",
            description = "앱 스토어 링크"
    )
    public String iOSStoreLink;

    @Schema(
            example = "This Application is...",
            description = "앱 소개"
    )
    @NotEmpty(message = "앱 소개글이 비어있을 수 없습니다.")
    public String body;
}
