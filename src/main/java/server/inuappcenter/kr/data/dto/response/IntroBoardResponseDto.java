package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.utils.BoardUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class IntroBoardResponseDto extends BoardResponseDto {
    public final String title;
    public final String subTitle;
    public final String androidStoreLink;
    public final String appleStoreLink;
    private final Map<Long, String> images;

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

    public static IntroBoardResponseDto entityToDto(HttpServletRequest request, Board board) {
        IntroBoard introBoard = createIntroBoardResponse(board);
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

    public static IntroBoard createIntroBoardResponse(Board board) {
        return (IntroBoard) board;
    }
}
