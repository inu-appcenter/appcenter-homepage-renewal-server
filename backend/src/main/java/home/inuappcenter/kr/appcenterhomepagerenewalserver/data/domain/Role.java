package home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.RoleRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.RoleResponseDto;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    private String role_name;

    public void setRole(RoleRequestDto roleRequestDto) {
        this.role_name = roleRequestDto.getRole_name();
    }

    public void setRole(Long id, RoleRequestDto roleRequestDto) {
        this.role_id = id;
        this.role_name = roleRequestDto.getRole_name();
    }

    public RoleResponseDto toRoleResponseDto(Role role) {
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setRoleResponseDto(role);
        return roleResponseDto;
    }
}
