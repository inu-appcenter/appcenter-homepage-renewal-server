package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhotoBoardRequestDto extends BoardRequestDto{
    @Schema(
            example = "2025 하반기 워크샵",
            description = "제목"
    )
    @NotBlank(message = "제목이 비어있을 수 없습니다.")
    private String title;

    @Schema(
            example = "2025-01-15",
            description = "활동 날짜"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eventDate;

    @Schema(
            description = "이미지 1장"
    )
    private MultipartFile multipartFile;

    @Override
    public Board createBoard() {
        return new PhotoBoard(this);
    }
}
