package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;

import javax.persistence.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoBoard extends Board {

    private String body;

    private List<Image> images = new ArrayList<>();

    public PhotoBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        this.body = photoBoardRequestDto.getBody();
        this.images = mappingPhotoAndEntity(photoBoardRequestDto.getMultipartFiles());
    }

    public void InjectImageListForTest(List<Image> images) {
        this.images = images;
    }

    public void updateBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        this.body = photoBoardRequestDto.getBody();
    }

    public PhotoBoardResponseDto toBoardResponseDto(PhotoBoard photoBoard, String image) {
        Map<Long,String> imageMap = new HashMap<>();
        imageMap.put(1L, image);
        return PhotoBoardResponseDto.builder()
                .board_id(photoBoard.getId())
                .images(imageMap)
                .body(photoBoard.getBody())
                .createdDate(photoBoard.getCreatedDate())
                .lastModifiedDate(photoBoard.getLastModifiedDate())
                .build();
    }

    public List<Image> mappingPhotoAndEntity(List<MultipartFile> multipartFiles) {
        List<Image> imageEntityList = new ArrayList<>();
        for (MultipartFile file: multipartFiles) {
            try {
                imageEntityList.add(new Image().returnMultipartToEntity(file));
            } catch (IOException e) {
                throw new RuntimeException("파일을 불러오는데 실패하였습니다.");
            }
        }
        imageEntityList.get(0).isThumbnail();
        return imageEntityList;
    }


    @Override
    public void modifyBoard(BoardRequestDto photoBoardRequestDto) {
        this.updateBoard((PhotoBoardRequestDto) photoBoardRequestDto);
    }

    @Override
    public void updateImage(List<Image> images) {
        this.images = images;
    }
}
