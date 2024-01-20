package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class GroupRequestDto {

    @Schema(
            example = "서버",
            description = "서버, 안드로이드, iOS등... 파트 중 하나"
    )
    @NotBlank(message = "파트가 비어있을 수 없습니다.")
    private String part;

    @Schema(
            example = "14",
            description = "13기, 14기, 15기등..."
    )
    @NotNull(message = "기수가 비어있을 수 없습니다.")
    private Double year;
}
