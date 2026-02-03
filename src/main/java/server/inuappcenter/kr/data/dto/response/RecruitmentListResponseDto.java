package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RecruitmentListResponseDto {
    private final Long id;
    private final String title;
    private final String thumbnail;
    private final Boolean isRecruiting;
    private final Long dDay;
    private final List<String> fieldNames;

    @Builder
    private RecruitmentListResponseDto(Long id, String title, String thumbnail,
                                       Boolean isRecruiting, Long dDay,
                                       List<String> fieldNames) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.isRecruiting = isRecruiting;
        this.dDay = dDay;
        this.fieldNames = fieldNames;
    }
}
