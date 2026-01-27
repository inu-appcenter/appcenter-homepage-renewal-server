package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentUpdateRequestDto {
    @Schema(example = "1", description = "콘텐츠 ID (수정 시 사용)")
    private Long contentId;

    @Schema(example = "첫째 날", description = "부제목")
    private String subTitle;

    @Schema(example = "팀빌딩 활동을 진행했습니다...", description = "내용")
    private String text;
}
