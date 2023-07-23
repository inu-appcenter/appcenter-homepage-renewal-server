package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.ImageRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.utils.ImageUtils;
import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "intro_board_id")
    private IntroBoard introBoard;

    @ManyToOne
    @JoinColumn(name = "photo_board_id")
    private PhotoBoard photoBoard ;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "image_data", columnDefinition = "MEDIUMBLOB")
    private byte[] imageData;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail = false;

    public Image(String originalFileName, byte[] imageData, Long fileSize, IntroBoard introBoard) {
        this.originalFileName = originalFileName;
        this.imageData = ImageUtils.compressImage(imageData);
        this.fileSize = fileSize;
        this.introBoard = introBoard;
    }

    public Image(String originalFileName, byte[] imageData, Long fileSize, PhotoBoard photoBoard) {
        this.originalFileName = originalFileName;
        this.imageData = ImageUtils.compressImage(imageData);
        this.fileSize = fileSize;
        this.photoBoard = photoBoard;
    }

    public List<Image> makeNewList() {
        return new ArrayList<>();
    }

    public List<Image> toList(ImageRequestDto imageRequestDto, IntroBoard introBoard) throws IOException {
        List<Image> imageList = this.makeNewList();
        for (MultipartFile file: imageRequestDto.getMultipartFileList()) {
            Image image = new Image(file.getOriginalFilename(), file.getBytes(), file.getSize(), introBoard);
            imageList.add(image);
        }
        return imageList;
    }

    public List<Image> toList(ImageRequestDto imageRequestDto, PhotoBoard photoBoard) throws IOException {
        List<Image> imageList = this.makeNewList();
        for (MultipartFile file: imageRequestDto.getMultipartFileList()) {
            Image image = new Image(file.getOriginalFilename(), file.getBytes(), file.getSize(), photoBoard);
            imageList.add(image);
        }
        return imageList;
    }

    // 실행시 이미지가 Thumbnail 속성을 가지고 있다는 것에 표시가 됨
    public void isThumbnail() {
        this.isThumbnail = true;
    }

    // 현재 위치를 반환하는 메소드
    public String getLocation(HttpServletRequest request, Image image) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  "/image/photo/" + image.getId().toString();
    }


}
