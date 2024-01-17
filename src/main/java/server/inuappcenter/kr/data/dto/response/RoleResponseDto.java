package server.inuappcenter.kr.data.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RoleResponseDto {
    private final Long roleId;
    private final String roleName;
    private final LocalDateTime createdDate;
    private final LocalDateTime lastModifiedDate;

    @Builder
    public RoleResponseDto(Long roleId, String roleName,
                           LocalDateTime createdDate, LocalDateTime lastModifiedDate) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.createdDate = createdDate;
        this.lastModifiedDate = lastModifiedDate;
    }
}
