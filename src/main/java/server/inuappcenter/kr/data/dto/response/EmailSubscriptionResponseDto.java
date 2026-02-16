package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;
import server.inuappcenter.kr.data.domain.EmailSubscription;

import java.time.LocalDateTime;

@Getter
@Builder
public class EmailSubscriptionResponseDto {
    private Long id;
    private String email;
    private LocalDateTime createdDate;

    public static EmailSubscriptionResponseDto entityToDto(EmailSubscription entity) {
        return EmailSubscriptionResponseDto.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .createdDate(entity.getCreatedDate())
                .build();
    }
}
