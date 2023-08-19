package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.PhotoBoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.PhotoBoardResponseDto;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class PhotoBoard extends Board{

    private String body;

    @OneToMany(mappedBy = "photoBoard")
    private List<Image> Images = new ArrayList<>();

    public void setPhotoBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        this.body = photoBoardRequestDto.getBody();
    }

    public PhotoBoardResponseDto<String> toBoardResponseDto(PhotoBoard photoBoard, String image) {
        PhotoBoardResponseDto<String> photoBoardResponseDto = new PhotoBoardResponseDto<>();
        photoBoardResponseDto.setPhotoBoardResponse(photoBoard, image);
        return photoBoardResponseDto;
    }
}
