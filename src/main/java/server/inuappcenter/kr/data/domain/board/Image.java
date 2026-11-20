package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.utils.ImageUtils;

import javax.persistence.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
    @JoinColumn(name = "board_id", updatable = false, insertable = false)
    private Board board;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "image_data", columnDefinition = "MEDIUMBLOB")
    private byte[] imageData;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail = false;

    // 이미지 내용이 바뀐 시각. URL에 ?v= 로 붙여 CDN/브라우저 캐시를 우회한다.
    // 이 컬럼 추가 전에 저장된 행은 null → URL이 기존과 동일하게 유지된다.
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Image(String originalFilename, byte[] bytes, long size) {
        this.originalFileName = originalFilename;
        this.imageData = bytes;
        this.fileSize = size;
        this.updatedAt = LocalDateTime.now();
    }

    public Image(MultipartFile multipartFile) {
        try {
            this.originalFileName = multipartFile.getOriginalFilename();
            this.imageData = ImageUtils.compressImage(multipartFile.getBytes());
            this.fileSize = multipartFile.getSize();
            this.updatedAt = LocalDateTime.now();
        } catch (IOException e) {
            throw new RuntimeException("사진을 처리하던 중 오류가 발생했습니다.");
        }

    }

    public void updateIdForTest(Long id) {
        this.id = id;
    }

    public void setImage(MultipartFile multipartFile) {
        try {
            this.originalFileName = multipartFile.getOriginalFilename();
            this.imageData = ImageUtils.compressImage(multipartFile.getBytes());
            this.fileSize = multipartFile.getSize();
            this.updatedAt = LocalDateTime.now();
        } catch (IOException e) {
            throw new RuntimeException("업로드 된 이미지의 정보를 불러올 수 없습니다.");
        }
    }

    // 호출시 이미지가 Thumbnail 속성을 가지고 있다는 것에 표시가 됨
    public void isThumbnail() {
        this.isThumbnail = true;
    }

    public Image returnMultipartToEntity(MultipartFile multipartFile) throws IOException {
        return new Image(multipartFile.getOriginalFilename(), ImageUtils.compressImage(multipartFile.getBytes()), multipartFile.getSize());
    }

    public void updateImage(MultipartFile multipartFile) {
        try {
            this.imageData = ImageUtils.compressImage(multipartFile.getBytes());
            this.fileSize = multipartFile.getSize();
            this.originalFileName = multipartFile.getOriginalFilename();
            this.updatedAt = LocalDateTime.now();
        } catch (IOException e) {
            throw new RuntimeException("사진을 처리하던 중 오류가 발생했습니다.");
        }
    }

    /**
     * 이미지 조회 경로. 내용이 바뀌면 ?v= 값이 바뀌어 새 URL이 된다.
     * updatedAt이 없는 기존 이미지는 "/image/photo/{id}" 그대로 반환한다.
     */
    public String getUrlPath() {
        String path = "/image/photo/" + id;
        if (updatedAt == null) {
            return path;
        }
        return path + "?v=" + updatedAt.toEpochSecond(ZoneOffset.UTC);
    }


}
