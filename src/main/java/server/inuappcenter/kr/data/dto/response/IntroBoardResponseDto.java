package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.utils.BoardUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class IntroBoardResponseDto {
    private final Long id;
    public final String title;
    public final String subTitle;
    public final String androidStoreLink;
    public final String appleStoreLink;
    public final String body;
    private final Map<Long, String> images;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private IntroBoardResponseDto (Long id, String title, String subTitle, String androidStoreLink, String appleStoreLink, String body, Map<Long, String> images,
                                  LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.androidStoreLink = androidStoreLink;
        this.appleStoreLink = appleStoreLink;
        this.body = body;
        this.images = images;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static IntroBoardResponseDto entityToDto(HttpServletRequest request, IntroBoard introBoard) {
        return IntroBoardResponseDto.builder()
                .id(introBoard.getId())
                .title(introBoard.getTitle())
                .subTitle(introBoard.getSubTitle())
                .androidStoreLink(introBoard.getAndroidStoreLink())
                .appleStoreLink(introBoard.getAppleStoreLink())
                .body(introBoard.getBody())
                .images(BoardUtils.returnImageURL(request, introBoard.getImages()))
                .createdDate(introBoard.getCreatedDate())
                .lastModifiedDate(introBoard.getLastModifiedDate())
                .build();
    }
}
