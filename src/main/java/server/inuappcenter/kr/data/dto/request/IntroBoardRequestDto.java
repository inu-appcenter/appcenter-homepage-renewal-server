package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.IntroBoard;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IntroBoardRequestDto extends BoardRequestDto{
    @Schema(
            example = "Test Application",
            description = "앱 제목"
    )
    @NotBlank(message = "앱 제목이 비어있을 수 없습니다.")
    private String title;

    @Schema(
            example = "Example Todo-list Application",
            description = "부 제목"
    )
    @NotBlank(message = "부 제목이 비어있을 수 없습니다.")
    private String subTitle;

    @Schema(
            example = "https://...",
            description = "플레이 스토어 링크"
    )
    private String androidStoreLink;

    @Schema(
            example = "https://...",
            description = "앱스토어 링크"
    )
    private String appleStoreLink;

    @Schema(
            example = "This Application is...",
            description = "앱 소개"
    )
    @NotBlank(message = "앱 소개글이 비어있을 수 없습니다.")
    private String body;

    @Schema(
            description = "이미지를 배열로 받습니다. '첫번째 요소는 아이콘입니다.'"
    )
    private List<MultipartFile> multipartFiles;

    @Override
    public Board createBoard() {
        return new IntroBoard(this);
    }
}
