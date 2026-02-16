package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.RegistrationCode;

import java.time.LocalDateTime;

@Getter
public class RegistrationCodeResponseDto {
    private final Long id;
    private final String code;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private RegistrationCodeResponseDto(Long id, String code, LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.id = id;
        this.code = code;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }

    public static RegistrationCodeResponseDto entityToDto(RegistrationCode registrationCode) {
        return RegistrationCodeResponseDto.builder()
                .id(registrationCode.getId())
                .code(registrationCode.getCode())
                .createdDate(registrationCode.getCreatedDate())
                .lastModifiedDate(registrationCode.getLastModifiedDate())
                .build();
    }
}
