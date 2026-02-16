package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.board.RecruitmentStatus;

import java.util.List;

@Getter
public class RecruitmentListResponseDto {
    private final Long id;
    private final String title;
    private final String thumbnail;
    private final RecruitmentStatus status;
    private final Long dDay;
    private final List<String> fieldNames;

    @Builder
    private RecruitmentListResponseDto(Long id, String title, String thumbnail,
                                       RecruitmentStatus status, Long dDay,
                                       List<String> fieldNames) {
        this.id = id;
        this.title = title;
        this.thumbnail = thumbnail;
        this.status = status;
        this.dDay = dDay;
        this.fieldNames = fieldNames;
    }
}
