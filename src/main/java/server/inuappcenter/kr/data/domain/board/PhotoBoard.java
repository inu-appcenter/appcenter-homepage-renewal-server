package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;
import server.inuappcenter.kr.data.utils.BoardUtils;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoBoard extends Board {

    private String body;

    private List<Image> images = new ArrayList<>();

    public PhotoBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        if (photoBoardRequestDto.getMultipartFiles() != null) {
            this.images = mappingPhotoAndEntity(photoBoardRequestDto.getMultipartFiles());
        }
        body = photoBoardRequestDto.getBody();
    }

    public void InjectImageListForTest(List<Image> images) {
        this.images = images;
    }

    public void updateBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        this.body = photoBoardRequestDto.getBody();
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
        this.images.addAll(images);
    }

    @Override
    public BoardResponseDto createResponse(HttpServletRequest request) {
        return PhotoBoardResponseDto.builder()
                .id(this.getId())
                .body(this.body)
                .images(BoardUtils.returnImageURL(request, this.images))
                .createdDate(this.getCreatedDate())
                .lastModifiedDate(this.getLastModifiedDate())
                .build();
    }
}
