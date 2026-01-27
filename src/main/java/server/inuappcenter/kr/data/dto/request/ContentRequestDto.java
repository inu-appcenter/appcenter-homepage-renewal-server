package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContentRequestDto {
    @Schema(example = "1", description = "콘텐츠 ID (수정 시 사용)")
    private Long contentId;

    @Schema(example = "첫째 날", description = "부제목")
    private String subTitle;

    @Schema(example = "팀빌딩 활동을 진행했습니다...", description = "내용")
    private String text;

    @Schema(description = "이미지 파일 목록")
    private List<MultipartFile> images;

    @Schema(example = "1", description = "순서")
    private Integer sequence;
}
