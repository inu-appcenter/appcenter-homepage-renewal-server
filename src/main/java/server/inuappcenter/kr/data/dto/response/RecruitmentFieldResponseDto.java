package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.RecruitmentField;

import java.time.LocalDateTime;

@Getter
public class RecruitmentFieldResponseDto {
    private final Long id;
    private final String name;

    @Builder
    private RecruitmentFieldResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static RecruitmentFieldResponseDto entityToDto(RecruitmentField field) {
        return RecruitmentFieldResponseDto.builder()
                .id(field.getId())
                .name(field.getName())
                .build();
    }
}
