package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

@Getter
public class IntroBoardResponseDto<T> {
    private final Long id;
    public final String title;
    public final String subTitle;
    public final String androidStoreLink;
    public final String iOSStoreLink;
    public final String body;
    private final T images;

    public IntroBoardResponseDto (Long id, String title, String subTitle, String androidStoreLink, String iOSStoreLink, String body, T images) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.androidStoreLink = androidStoreLink;
        this.iOSStoreLink = iOSStoreLink;
        this.body = body;
        this.images = images;
    }
}
