package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.PhotoBoard;
import server.inuappcenter.kr.data.utils.BoardUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class PhotoBoardResponseDto {
    private final Long board_id;
    private final String body;
    private final Map<Long, String> images;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private PhotoBoardResponseDto (Long board_id, String body, Map<Long, String> images, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.board_id = board_id;
        this.body = body;
        this.images = images;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static PhotoBoardResponseDto entityToDto(HttpServletRequest request, Board board) {
        PhotoBoard photoBoard = createBoard(board);
        return PhotoBoardResponseDto.builder()
                .board_id(photoBoard.getId())
                .body(photoBoard.getBody())
                .images(BoardUtils.returnImageURL(request, photoBoard.getImages()))
                .createdDate(photoBoard.getCreatedDate())
                .lastModifiedDate(photoBoard.getLastModifiedDate())
                .build();
    }

    public static PhotoBoard createBoard(Board board) {
        return (PhotoBoard) board;
    }
}
