package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 활동 게시판
public class PhotoBoard extends Board {

    private LocalDate eventDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    public PhotoBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        if (photoBoardRequestDto.getMultipartFile() != null && !photoBoardRequestDto.getMultipartFile().isEmpty()) {
            this.image = new Image(photoBoardRequestDto.getMultipartFile());
        }
        this.body = photoBoardRequestDto.getTitle();
        this.eventDate = photoBoardRequestDto.getEventDate();
    }

    public void updateBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        if (photoBoardRequestDto.getTitle() != null) {
            this.body = photoBoardRequestDto.getTitle();
        }
        if (photoBoardRequestDto.getEventDate() != null) {
            this.eventDate = photoBoardRequestDto.getEventDate();
        }
        if (photoBoardRequestDto.getMultipartFile() != null && !photoBoardRequestDto.getMultipartFile().isEmpty()) {
            this.image = new Image(photoBoardRequestDto.getMultipartFile());
        }
    }

    @Override
    public void modifyBoard(BoardRequestDto photoBoardRequestDto) {
        this.updateBoard((PhotoBoardRequestDto) photoBoardRequestDto);
    }

    @Override
    public void updateImage(List<Image> images) {
        if (images != null && !images.isEmpty()) {
            this.image = images.get(0);
        }
    }

    @Override
    public List<Image> getImages() {
        return this.image != null ? Collections.singletonList(this.image) : Collections.emptyList();
    }

    public String getTitle() {
        return this.body;
    }

    @Override
    public BoardResponseDto createResponse(HttpServletRequest request) {
        String imageUrl = null;
        if (this.image != null) {
            imageUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                    + "/image/photo/" + this.image.getId();
        }
        return PhotoBoardResponseDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .eventDate(this.eventDate)
                .imageUrl(imageUrl)
                .createdDate(this.getCreatedDate())
                .lastModifiedDate(this.getLastModifiedDate())
                .build();
    }
}
