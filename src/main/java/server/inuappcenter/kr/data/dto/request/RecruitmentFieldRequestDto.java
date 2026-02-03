package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecruitmentFieldRequestDto {
    @Schema(example = "Android", description = "모집 분야 이름")
    @NotBlank(message = "모집 분야 이름이 비어있을 수 없습니다.")
    private String name;
}
