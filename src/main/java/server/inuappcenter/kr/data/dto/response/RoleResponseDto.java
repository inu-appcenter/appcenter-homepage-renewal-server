package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoleResponseDto {
    private final Long role_id;
    private final String role_name;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    private RoleResponseDto(Long role_id, String role_name,
                            LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.role_id = role_id;
        this.role_name = role_name;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
