package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.IntroBoard;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IntroBoardResponseDto<T> {
    private Long id;
    public String title;
    public String subTitle;
    public String androidStoreLink;
    public String iOSStoreLink;
    public String body;
    private T images;

    public void setIntroBoardResponse(IntroBoard introBoard, T images) {
        this.id = introBoard.getId();
        this.title = introBoard.getTitle();
        this.subTitle = introBoard.getSubTitle();
        this.androidStoreLink = introBoard.getAndroidStoreLink();
        this.iOSStoreLink = introBoard.getIOSStoreLink();
        this.body = introBoard.getBody();
        this.images = images;
    }
}
