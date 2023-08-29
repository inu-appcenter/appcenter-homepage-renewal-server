package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class ImageRequestDto {
    @Schema(
            description = "이미지 리스트(MultipartFile)"
    )
    @NotBlank(message = "이미지가 비어있을 수 없습니다. (최소 1개 이상의 이미지가 필요합니다)")
    private List<MultipartFile> multipartFileList;

    public ImageRequestDto(List<MultipartFile> multipartFileList) {
        this.multipartFileList = multipartFileList;
    }
}
