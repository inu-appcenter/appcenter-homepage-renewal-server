package server.inuappcenter.kr.data.dto.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public abstract class BoardRequestDto {
    String body;
    private List<MultipartFile> multipartFiles;
}
