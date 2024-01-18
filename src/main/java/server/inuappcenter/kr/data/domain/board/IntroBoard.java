package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;

import javax.persistence.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntroBoard extends Board {
    public String title;
    public String subTitle;
    public String androidStoreLink;
    public String appleStoreLink;
    public String body;

    private List<Image> images = new ArrayList<>();

    public IntroBoard(IntroBoardRequestDto introBoardRequestDto) {
        if (introBoardRequestDto.getMultipartFiles() != null) {
            this.images = mappingPhotoAndEntity(introBoardRequestDto.getMultipartFiles());
        }
        this.title = introBoardRequestDto.getTitle();
        this.subTitle = introBoardRequestDto.getSubTitle();
        this.androidStoreLink = introBoardRequestDto.getAndroidStoreLink();
        this.appleStoreLink = introBoardRequestDto.getAppleStoreLink();
        this.body = introBoardRequestDto.getBody();
    }

    public void InjectImageListForTest(List<Image> images) {
        this.images = images;
    }

    public void updateBoard(IntroBoardRequestDto introBoardRequestDto) {
        this.title = introBoardRequestDto.getTitle();
        this.subTitle = introBoardRequestDto.getSubTitle();
        this.androidStoreLink = introBoardRequestDto.getAndroidStoreLink();
        this.appleStoreLink = introBoardRequestDto.getAppleStoreLink();
        this.body = introBoardRequestDto.getBody();
    }

    public IntroBoardResponseDto toBoardResponseDto(IntroBoard introBoard, String image) {
        Map<Long,String> imageMap = new HashMap<>();
        imageMap.put(1L, image);
        return IntroBoardResponseDto.builder()
                .id(introBoard.getId())
                .androidStoreLink(introBoard.getAndroidStoreLink())
                .appleStoreLink(introBoard.getAppleStoreLink())
                .body(introBoard.getBody())
                .createdDate(introBoard.getCreatedDate())
                .images(imageMap)
                .lastModifiedDate(introBoard.getLastModifiedDate())
                .subTitle(introBoard.getSubTitle())
                .title(introBoard.getTitle())
                .build();
    }

    // 새 이미지 객체를 만들어 PhotoBoard(부모객체)와 매핑시킵니다.
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
    public void modifyBoard(BoardRequestDto boardRequestDto) {
        // updateBoard 메소드를 통해 IntroBoard의 내용을 업데이트 합니다.
        this.updateBoard((IntroBoardRequestDto) boardRequestDto);
    }

    @Override
    public void updateImage(List<Image> images) {
        this.images.addAll(images);
    }
}
