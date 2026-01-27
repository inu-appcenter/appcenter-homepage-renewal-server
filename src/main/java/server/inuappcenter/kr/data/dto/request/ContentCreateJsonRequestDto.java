package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentCreateJsonRequestDto {
    @Schema(example = "첫째 날", description = "부제목")
    private String subTitle;

    @Schema(example = "팀빌딩 활동을 진행했습니다...", description = "내용")
    private String text;

    @Schema(example = "1", description = "순서")
    private Integer sequence;

    // 갯수 구분 위함.
    @Schema(example = "[0,1]", description = "contentImages 배열에서 사용할 이미지 인덱스 목록 (없으면 이미지 없음)")
    private List<Integer> imageIndexes;
}
