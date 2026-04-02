package server.inuappcenter.kr.data.dto.response;

import lombok.Getter;

@Getter
public class MemberProjectInfoDto {
    private final Long id;
    private final String title;
    private final String mainImage;

    public MemberProjectInfoDto(Long id, String title, String mainImage) {
        this.id = id;
        this.title = title;
        this.mainImage = mainImage;
    }
}
