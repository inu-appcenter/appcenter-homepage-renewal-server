package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.board.ActivityBoard;
import server.inuappcenter.kr.data.domain.board.Board;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ActivityBoardRequestDto extends BoardRequestDto {
    @Schema(example = "2025 제 1회 세미나", description = "활동 제목")
    @NotBlank(message = "제목이 비어있을 수 없습니다.")
    private String title;

    @Schema(example = "제 1회 세미나입니다.", description = "활동 설명")
    private String body;

    @Schema(description = "대표 이미지")
    private MultipartFile thumbnail;

    @Schema(example = "17기 센터장",description = "작성자")
    private String author;

    @Schema(description = "활동 내용 목록 (subTitle, text, image, sequence)")
    private List<ContentRequestDto> contents;

    @Override
    public Board createBoard() {
        return new ActivityBoard(this);
    }
}
