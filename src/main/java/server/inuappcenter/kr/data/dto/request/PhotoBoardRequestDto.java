package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PhotoBoardRequestDto extends BoardRequestDto{
    @Schema(
            example = "안녕하세요",
            description = "본문"
    )
    @NotBlank(message = "본문이 비어있을 수 없습니다.")
    String body;

    @Schema(
            description = "이미지를 배열로 받습니다."
    )
    private List<MultipartFile> multipartFiles;

    @Override
    public Board createBoard() {
        return new PhotoBoard(this);
    }
}
