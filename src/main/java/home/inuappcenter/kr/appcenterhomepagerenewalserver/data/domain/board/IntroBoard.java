package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.IntroBoardRequestDto;
import javax.persistence.*;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.IntroBoardResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class IntroBoard extends Board {
    public String title;
    public String subTitle;
    public String androidStoreLink;
    public String iOSStoreLink;
    public String body;

    @OneToMany(mappedBy = "introBoard")
    private List<Image> Images = new ArrayList<>();

    public void setIntroBoard(IntroBoardRequestDto introBoardRequestDto) {
        this.title = introBoardRequestDto.getTitle();
        this.subTitle = introBoardRequestDto.getSubTitle();
        this.androidStoreLink = introBoardRequestDto.getAndroidStoreLink();
        this.iOSStoreLink = introBoardRequestDto.getIOSStoreLink();
        this.body = introBoardRequestDto.getBody();
    }

    public IntroBoardResponseDto<String> toBoardResponseDto(IntroBoard introBoard, String image) {
        return IntroBoardResponseDto.<String>builder()
                .id(introBoard.getId())
                .title(introBoard.getTitle())
                .subTitle(introBoard.getSubTitle())
                .androidStoreLink(introBoard.getAndroidStoreLink())
                .iOSStoreLink(introBoard.getIOSStoreLink())
                .body(introBoard.getBody())
                .images(image)
                .build();
    }
}
