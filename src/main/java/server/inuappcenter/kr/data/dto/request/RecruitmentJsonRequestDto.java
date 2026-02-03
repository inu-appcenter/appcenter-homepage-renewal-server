package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RecruitmentJsonRequestDto {
    @Schema(example = "2025년 1학기 신입 부원 모집", description = "모집 제목")
    @NotBlank(message = "제목이 비어있을 수 없습니다.")
    private String title;

    @Schema(example = "앱센터에서 신입 부원을 모집합니다!", description = "모집 소개글")
    private String body;

    @Schema(example = "2026-03-01", description = "모집 시작일")
    @NotNull(message = "시작일이 비어있을 수 없습니다.")
    private LocalDate startDate;

    @Schema(example = "2026-03-15", description = "모집 마감일")
    @NotNull(message = "마감일이 비어있을 수 없습니다.")
    private LocalDate endDate;

    @Schema(example = "10", description = "모집 인원")
    private Integer capacity;

    @Schema(example = "인천대학교 재학생", description = "모집 대상")
    private String targetAudience;

    @Schema(example = "https://forms.google.com/...", description = "지원 링크")
    private String applyLink;

    @Schema(example = "[1, 2, 3]", description = "모집 분야 ID 목록")
    private List<Long> fieldIds;
}
