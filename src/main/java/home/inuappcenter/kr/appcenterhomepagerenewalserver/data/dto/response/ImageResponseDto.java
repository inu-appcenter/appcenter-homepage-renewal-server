package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.PhotoBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class ImageResponseDto {
    private Long id;
    private IntroBoard introBoard;
    private PhotoBoard photoBoard ;
    private String originalFileName;
    // 데이터를 받아오지 않음
    private Long fileSize;
    private Boolean isThumbnail = false;

    public void setImageResponseDto(Image image) {
        this.id = image.getId();
        this.introBoard = image.getIntroBoard();
        this.photoBoard = image.getPhotoBoard();
        this.originalFileName = image.getOriginalFileName();
        this.fileSize = image.getFileSize();
        this.isThumbnail = image.getIsThumbnail();
    }
}
