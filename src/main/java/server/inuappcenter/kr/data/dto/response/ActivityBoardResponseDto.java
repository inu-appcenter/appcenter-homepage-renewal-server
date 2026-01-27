package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.Image;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ActivityBoardResponseDto extends BoardResponseDto {
    private final String title;
    private final String author;
    private final String thumbnail;
    private final List<ActivityContentResponseDto> contents;

    @Builder
    private ActivityBoardResponseDto(Long id, String title, String body,
                                     String author, String thumbnail,
                                     List<ActivityContentResponseDto> contents,
                                     LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.author = author;
        this.thumbnail = thumbnail;
        this.contents = contents;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
