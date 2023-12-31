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
    private Long role_id;

    private String role_name;

    public Role(RoleRequestDto roleRequestDto) {
        this.role_name = roleRequestDto.getRole_name();
    }

    public void setRole(RoleRequestDto roleRequestDto) {
        this.role_name = roleRequestDto.getRole_name();
    }

    public RoleResponseDto toRoleResponseDto(Role role) {
        return RoleResponseDto.builder()
                .role_id(role.getRole_id())
                .role_name(role.getRole_name())
                .lastModifiedDate(role.getLastModifiedDate())
                .createdDate(role.getCreatedDate())
                .build();
    }
}
