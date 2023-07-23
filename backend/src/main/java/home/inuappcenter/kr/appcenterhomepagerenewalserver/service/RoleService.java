package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Role;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.RoleRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.RoleResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final GroupService groupService;

    public RoleResponseDto getRole(Long id) throws Exception {
        Role getRole = roleRepository.findById(id).orElseThrow(Exception::new);
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setRoleResponseDto(getRole);
        return roleResponseDto;
    }

    public RoleResponseDto saveRole(RoleRequestDto roleRequestDto) {
        Role role = new Role();
        role.setRole(roleRequestDto);
        Role savedRole = roleRepository.save(role);
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setRoleResponseDto(savedRole);
        return roleResponseDto;
    }

    public RoleResponseDto updateRole(Long id, RoleRequestDto roleRequestDto) throws Exception {
        Role found_role = roleRepository.findById(id).orElseThrow(Exception::new);
        found_role.setRole(id, roleRequestDto);
        Role update_role = roleRepository.save(found_role);
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setRoleResponseDto(update_role);
        return roleResponseDto;
    }

    public List<RoleResponseDto> findAllRole() {
        List<Role> found_roles = roleRepository.findAll();
        return found_roles.stream()
                .map(data -> data.toRoleResponseDto(data))
                .collect(Collectors.toList());
    }

    public String deleteRole(Long id) throws Exception {
        Role found_role = roleRepository.findById(id).orElseThrow(Exception::new);
        if(!groupService.findRole(found_role)) {
            roleRepository.deleteById(id);
            return "role id ["+ id + "] has been deleted.";
        } else {
            return "The role [" + id + "] is part of a Group. Please delete the Group first";
        }
    }
}
