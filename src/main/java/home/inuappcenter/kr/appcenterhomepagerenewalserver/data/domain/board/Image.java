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

    public <T>Image(String originalFileName, byte[] imageData, Long fileSize, T board) {
        this.originalFileName = originalFileName;
        this.imageData = ImageUtils.compressImage(imageData);
        this.fileSize = fileSize;

        if (board instanceof IntroBoard) {
            this.introBoard = (IntroBoard) board;
        } else {
            this.photoBoard = (PhotoBoard) board;
        }
    }

    public void setImage(MultipartFile multipartFile) {
        try {
            this.originalFileName = multipartFile.getOriginalFilename();
            this.imageData = ImageUtils.compressImage(multipartFile.getBytes());
            this.fileSize = multipartFile.getSize();
        } catch (IOException e) {
            throw new RuntimeException("업로드 된 이미지의 정보를 불러올 수 없습니다.");
        }
    }

    public List<Image> makeNewList() {
        return new ArrayList<>();
    }

    public <T> List<Image> toList(ImageRequestDto imageRequestDto, T board) {
        List<Image> imageList = this.makeNewList();
        for (MultipartFile file: imageRequestDto.getMultipartFileList()) {
            try {
                Image image = new Image(file.getOriginalFilename(), file.getBytes(), file.getSize(), board);
                imageList.add(image);
            } catch (IOException e) {
                throw new RuntimeException("파일을 불러오는데 실패하였습니다.");
            }
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
