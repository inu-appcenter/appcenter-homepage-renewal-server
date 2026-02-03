package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RecruitmentResponseDto extends BoardResponseDto {
    private final String title;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer capacity;
    private final String targetAudience;
    private final String applyLink;
    private final String thumbnail;
    private final Boolean isRecruiting;
    private final Boolean forceClosed;
    private final Long dDay;
    private final List<RecruitmentFieldResponseDto> fields;

    @Builder
    private RecruitmentResponseDto(Long id, String title, String body,
                                   LocalDate startDate, LocalDate endDate,
                                   Integer capacity, String targetAudience, String applyLink,
                                   String thumbnail, Boolean isRecruiting, Boolean forceClosed, Long dDay,
                                   List<RecruitmentFieldResponseDto> fields,
                                   LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
        this.targetAudience = targetAudience;
        this.applyLink = applyLink;
        this.thumbnail = thumbnail;
        this.isRecruiting = isRecruiting;
        this.forceClosed = forceClosed;
        this.dDay = dDay;
        this.fields = fields;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
