package server.inuappcenter.kr.data.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class MemberProjectInfoDto {
    private final Long id;
    private final String title;
    private final String mainImage;

    @JsonCreator
    public MemberProjectInfoDto(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("mainImage") String mainImage
    ) {
        this.id = id;
        this.title = title;
        this.mainImage = mainImage;
    }
}
