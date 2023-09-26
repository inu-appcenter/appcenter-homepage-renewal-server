package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IntroBoardResponseDto<T> {
    private final Long id;
    public final String title;
    public final String subTitle;
    public final String androidStoreLink;
    public final String appleStoreLink;
    public final String body;
    private final T images;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    public IntroBoardResponseDto (Long id, String title, String subTitle, String androidStoreLink, String appleStoreLink, String body, T images,
                                  LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.androidStoreLink = androidStoreLink;
        this.appleStoreLink = appleStoreLink;
        this.body = body;
        this.images = images;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
