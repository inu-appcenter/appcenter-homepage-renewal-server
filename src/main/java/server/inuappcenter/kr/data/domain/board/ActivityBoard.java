package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.dto.request.ActivityBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityBoard extends Board {
    private String title;

    private String titleEng;

    private String author;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "thumbnail_id")
    private Image thumbnail;

    public ActivityBoard(ActivityBoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.titleEng = requestDto.getTitleEng();
        this.body = requestDto.getBody();
        this.author = requestDto.getAuthor();
        if (requestDto.getThumbnail() != null && !requestDto.getThumbnail().isEmpty()) {
            this.thumbnail = new Image(requestDto.getThumbnail());
        }
    }

    public void updateBoard(ActivityBoardRequestDto requestDto) {
        if (requestDto.getTitle() != null) {
            this.title = requestDto.getTitle();
        }
        if (requestDto.getTitleEng() != null) {
            this.titleEng = requestDto.getTitleEng();
        }
        if (requestDto.getBody() != null) {
            this.body = requestDto.getBody();
        }
        if (requestDto.getAuthor() != null) {
            this.author = requestDto.getAuthor();
        }
    }

    public void updateThumbnail(MultipartFile thumbnailFile) {
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            if (this.thumbnail == null) {
                this.thumbnail = new Image(thumbnailFile);
            } else {
                this.thumbnail.updateImage(thumbnailFile);
            }
        }
    }

    @Override
    public void modifyBoard(BoardRequestDto boardRequestDto) {
        this.updateBoard((ActivityBoardRequestDto) boardRequestDto);
    }

    @Override
    public void updateImage(List<Image> images) {
        // ActivityBoard는 ActivityContent를 통해 이미지 관리
    }

    @Override
    public BoardResponseDto createResponse(HttpServletRequest request) {
        // Service에서 contents를 조회하여 Response 생성
        return null;
    }
}
