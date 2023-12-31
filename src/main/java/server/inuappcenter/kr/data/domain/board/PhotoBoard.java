package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoBoard extends Board {

    private String body;

    @OneToMany(mappedBy = "photoBoard")
    private final List<Image> images = new ArrayList<>();

    public PhotoBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        this.body = photoBoardRequestDto.getBody();
    }

    public void updateBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        this.body = photoBoardRequestDto.getBody();
    }

    public PhotoBoardResponseDto<String> toBoardResponseDto(PhotoBoard photoBoard, String image) {
        return new PhotoBoardResponseDto<>(
                photoBoard.getId(),
                photoBoard.getBody(),
                image,
                photoBoard.getCreatedDate(),
                photoBoard.getLastModifiedDate()
        );
    }
}
