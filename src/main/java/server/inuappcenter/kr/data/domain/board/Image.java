package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.utils.ImageUtils;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "intro_board_id", updatable = false, insertable = false)
    private IntroBoard introBoard;

    @ManyToOne
    @JoinColumn(name = "photo_board_id", updatable = false, insertable = false)
    private PhotoBoard photoBoard ;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "image_data", columnDefinition = "MEDIUMBLOB")
    private byte[] imageData;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail = false;

    public Image(String originalFilename, byte[] bytes, long size) {
        this.originalFileName = originalFilename;
        this.imageData = bytes;
        this.fileSize = size;
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

    // 호출시 이미지가 Thumbnail 속성을 가지고 있다는 것에 표시가 됨
    public void isThumbnail() {
        this.isThumbnail = true;
    }

    // 자원의 현재 위치를 반환하는 메소드
    public String getLocation(HttpServletRequest request, Image image) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() +  "/image/photo/" + image.getId().toString();
    }

    public Image returnMultipartToEntity(MultipartFile multipartFile) throws IOException {
        return new Image(multipartFile.getOriginalFilename(), multipartFile.getBytes(), multipartFile.getSize());
    }


}
