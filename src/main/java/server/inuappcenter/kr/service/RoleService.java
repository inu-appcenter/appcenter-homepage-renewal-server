package server.inuappcenter.kr.service;

import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Role;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.RoleResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.RoleRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;

    @Transactional
    public RoleResponseDto getRole(Long id) {
        Role getRole = roleRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return RoleResponseDto.builder()
                .role_id(getRole.getRole_id())
                .role_name(getRole.getRole_name())
                .build();
    }

    public RoleResponseDto saveRole(RoleRequestDto roleRequestDto) {
        Role role = new Role();
        role.setRole(roleRequestDto);
        Role savedRole = roleRepository.save(role);
        return RoleResponseDto.builder()
                .role_id(savedRole.getRole_id())
                .role_name(savedRole.getRole_name())
                .build();
    }

    @Transactional
    public RoleResponseDto updateRole(Long id, RoleRequestDto roleRequestDto) {
        Role found_role = roleRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        found_role.setRole(id, roleRequestDto);
        Role update_role = roleRepository.save(found_role);
        return RoleResponseDto.builder()
                .role_id(update_role.getRole_id())
                .role_name(update_role.getRole_name())
                .build();
    }

    public List<RoleResponseDto> findAllRole() {
        List<Role> found_roles = roleRepository.findAll();
        return found_roles.stream()
                .map(data -> data.toRoleResponseDto(data))
                .collect(Collectors.toList());
    }

    @Transactional
    public String deleteRole(Long id) {
        Role found_role = roleRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        ArrayList<Group> found_groups = groupRepository.findAllByRole(found_role);
        if (found_groups.isEmpty()) {
            roleRepository.deleteById(id);
            return "role id [" + id + "] has been deleted.";
        } else {
            return "The role [" + id + "] is part of a Group. Please delete the Group first";
        }
    }
}
