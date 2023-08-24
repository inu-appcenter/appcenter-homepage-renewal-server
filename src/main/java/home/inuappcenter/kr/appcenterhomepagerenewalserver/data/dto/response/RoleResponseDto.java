package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoleResponseDto {
    private final Long role_id;
    private final String role_name;

    @Builder
    private RoleResponseDto(Long role_id, String role_name) {
        this.role_id = role_id;
        this.role_name = role_name;
    }
}
