package server.inuappcenter.kr.data.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class PhotoBoardResponseDto extends BoardResponseDto{
    private final String title;
    private final LocalDate eventDate;
    private final String imageUrl;

    @JsonIgnore
    @Override
    public String getBody() {
        return null;
    }

    @Builder
    private PhotoBoardResponseDto (Long id, String title, LocalDate eventDate, String imageUrl,
                                   LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.imageUrl = imageUrl;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static PhotoBoardResponseDto entityToDto(HttpServletRequest request, Board board) {
        PhotoBoard photoBoard = createBoard(board);
        String imageUrl = null;
        if (photoBoard.getImage() != null) {
            imageUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/photo/" + photoBoard.getImage().getId();
        }
        return PhotoBoardResponseDto.builder()
                .id(photoBoard.getId())
                .title(photoBoard.getTitle())
                .eventDate(photoBoard.getEventDate())
                .imageUrl(imageUrl)
                .createdDate(photoBoard.getCreatedDate())
                .lastModifiedDate(photoBoard.getLastModifiedDate())
                .build();
    }

    public static PhotoBoard createBoard(Board board) {
        return (PhotoBoard) board;
    }
}
