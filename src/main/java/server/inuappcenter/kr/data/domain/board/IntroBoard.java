package server.inuappcenter.kr.data.domain.board;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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

    @OneToMany
    @JoinColumn(name = "intro_board_id")
    private final List<Image> Images = new ArrayList<>();

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
}
