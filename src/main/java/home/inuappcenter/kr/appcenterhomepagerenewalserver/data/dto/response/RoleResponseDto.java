package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RoleResponseDto {
    private Long role_id;
    private String role_name;

    public void setRoleResponseDto(Role role) {
        this.role_id = role.getRole_id();
        this.role_name = role.getRole_name();
    }
}
