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
            description = "리스트(MultipartFile)"
    )
    @NotBlank
    private List<MultipartFile> multipartFileList;

    public ImageRequestDto(List<MultipartFile> multipartFileList) {
        this.multipartFileList = multipartFileList;
    }
}
