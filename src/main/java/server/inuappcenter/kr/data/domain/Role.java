package server.inuappcenter.kr.data.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import server.inuappcenter.kr.common.data.domain.BaseTimeEntity;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.RoleResponseDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roleName;

    public Role(Long id, String roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Role(RoleRequestDto roleRequestDto) {
        this.roleName = roleRequestDto.getRoleName();
    }

    public void setRole(RoleRequestDto roleRequestDto) {
        this.roleName = roleRequestDto.getRoleName();
    }

    public RoleResponseDto toRoleResponseDto(Role role) {
        return RoleResponseDto.builder()
                .roleId(role.getId())
                .roleName(role.getRoleName())
                .lastModifiedDate(role.getLastModifiedDate())
                .createdDate(role.getCreatedDate())
                .build();
    }
}
