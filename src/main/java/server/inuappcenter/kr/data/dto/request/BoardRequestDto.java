package server.inuappcenter.kr.data.dto.request;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.board.Board;

import java.util.List;

@Getter
public abstract class BoardRequestDto {
    @Hidden
    String body;
    @Hidden
    private List<MultipartFile> multipartFiles;
    public abstract Board createBoard();
}
