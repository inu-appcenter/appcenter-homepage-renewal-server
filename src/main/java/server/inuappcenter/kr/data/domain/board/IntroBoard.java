package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IntroBoard extends Board {
    public String title;
    public String subTitle;
    public String androidStoreLink;
    public String appleStoreLink;
    public String body;

    @OneToMany(mappedBy = "introBoard")
    private List<Image> Images = new ArrayList<>();

    public IntroBoard(IntroBoardRequestDto introBoardRequestDto) {
        this.title = introBoardRequestDto.getTitle();
        this.subTitle = introBoardRequestDto.getSubTitle();
        this.androidStoreLink = introBoardRequestDto.getAndroidStoreLink();
        this.appleStoreLink = introBoardRequestDto.getAppleStoreLink();
        this.body = introBoardRequestDto.getBody();
    }

    public void updateBoard(IntroBoardRequestDto introBoardRequestDto) {
        this.title = introBoardRequestDto.getTitle();
        this.subTitle = introBoardRequestDto.getSubTitle();
        this.androidStoreLink = introBoardRequestDto.getAndroidStoreLink();
        this.appleStoreLink = introBoardRequestDto.getAppleStoreLink();
        this.body = introBoardRequestDto.getBody();
    }

    public IntroBoardResponseDto<String> toBoardResponseDto(IntroBoard introBoard, String image) {
        return new IntroBoardResponseDto<>(
                introBoard.getId(),
                introBoard.getTitle(),
                introBoard.getSubTitle(),
                introBoard.getAndroidStoreLink(),
                introBoard.getAppleStoreLink(),
                introBoard.getBody(),
                image,
                introBoard.getCreatedDate(),
                introBoard.getLastModifiedDate()
        );
    }
}
