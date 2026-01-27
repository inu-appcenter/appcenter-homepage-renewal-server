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
public class ActivityBoardUpdateJsonRequestDto {
    @Schema(example = "2025 제 1회 세미나", description = "활동 제목")
    private String title;

    @Schema(example = "제 1회 세미나입니다.", description = "활동 설명")
    private String body;

    @Schema(example = "17기 센터장", description = "작성자")
    private String author;

    @Schema(description = "활동 내용 목록 (contentId, subTitle, text)")
    private List<ContentUpdateRequestDto> contents;
}
