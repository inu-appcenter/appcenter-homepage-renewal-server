package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "이미지가 최소 1개 이상 필요합니다. (첫번째 이미지는 썸네일입니다.)")
    private List<MultipartFile> multipartFiles;
}
