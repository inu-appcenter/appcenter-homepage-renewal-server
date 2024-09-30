package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Role;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.RoleResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.RoleRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;

    @Transactional(readOnly = true)
    public RoleResponseDto getRole(Long id) {
        Role getRole = roleRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return RoleResponseDto.builder()
                .roleId(getRole.getId())
                .roleName(getRole.getRoleName())
                .createdDate(getRole.getCreatedDate())
                .lastModifiedDate(getRole.getLastModifiedDate())
                .build();
    }

    @Transactional
    public RoleResponseDto saveRole(RoleRequestDto roleRequestDto) {
        Role role = new Role(roleRequestDto);
        Role savedRole = roleRepository.save(role);
        return RoleResponseDto.builder()
                .roleId(savedRole.getId())
                .roleName(savedRole.getRoleName())
                .createdDate(savedRole.getCreatedDate())
                .lastModifiedDate(savedRole.getLastModifiedDate())
                .build();
    }

    @Transactional
    public RoleResponseDto updateRole(Long id, RoleRequestDto roleRequestDto) {
        Role found_role = roleRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        found_role.setRole(roleRequestDto);
        Role update_role = roleRepository.save(found_role);
        return RoleResponseDto.builder()
                .roleId(update_role.getId())
                .roleName(update_role.getRoleName())
                .createdDate(update_role.getCreatedDate())
                .lastModifiedDate(update_role.getLastModifiedDate())
                .build();
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDto> findAllRole() {
        List<Role> found_roles = roleRepository.findAll();
        return found_roles.stream()
                .map(data -> data.toRoleResponseDto(data))
                .collect(Collectors.toList());
    }

    @Transactional
    public CommonResponseDto deleteRole(Long id) {
        Role found_role = roleRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        ArrayList<Group> found_groups = groupRepository.findAllByRole(found_role);
        if (found_groups.isEmpty()) {
            roleRepository.deleteById(id);
            return new CommonResponseDto("role id [" + id + "] has been deleted.");
        } else {
            return new CommonResponseDto("The role [" + id + "] is part of a Group. Please delete the Group first");
        }
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDto> findIdByName(String name) {
        List<Role> foundGroups = roleRepository.findAllByRoleName(name);
        return foundGroups.stream()
                .map(data -> data.toRoleResponseDto(data))
                .collect(Collectors.toList());
    }
}
