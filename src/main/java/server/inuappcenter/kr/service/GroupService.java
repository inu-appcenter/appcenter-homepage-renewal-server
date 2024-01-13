package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.Role;
import server.inuappcenter.kr.data.dto.request.GroupRequestDto;
import server.inuappcenter.kr.data.dto.response.GroupResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.MemberRepository;
import server.inuappcenter.kr.data.repository.RoleRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public GroupResponseDto getGroup(Long id) {
        Group foundGroup = groupRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return GroupResponseDto.entityToDto(foundGroup);
    }

    @Transactional(readOnly = true)
    public List<GroupResponseDto> findAllGroup() {
        List<Group> found_Groups = groupRepository.findAll();
        return found_Groups.stream()
                .map(data -> data.toGroupResponseDto(data))
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupResponseDto assignGroup(Long member_id, Long role_id, GroupRequestDto groupRequestDto) {
        Member found_member = memberRepository.findById(member_id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        Role found_role = roleRepository.findById(role_id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        Group group = new Group(found_member, found_role, groupRequestDto);

        Group savedGroup = groupRepository.save(group);
        return GroupResponseDto.entityToDto(savedGroup);
    }

    @Transactional
    public GroupResponseDto updateGroup(GroupRequestDto groupRequestDto, Long id) {
        Group foundGroup = groupRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        foundGroup.setGroup(id, groupRequestDto);
        Group savedGroup = groupRepository.save(foundGroup);
        return GroupResponseDto.entityToDto(savedGroup);
    }


    public CommonResponseDto deleteGroup(Long id) {
        groupRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

    public CommonResponseDto deleteMultipleGroups(List<Long> id) {
        groupRepository.deleteAllByIdInBatch(id);
        return new CommonResponseDto("id: " + id + " have been successfully deleted.");
    }

    public List<GroupResponseDto> searchByMemberName(String name) {
        List<Group> foundGroups = groupRepository.findAllByMember_Name(name);
        return foundGroups.stream()
                .map(data -> data.toGroupResponseDto(data))
                .collect(Collectors.toList());
    }
}
