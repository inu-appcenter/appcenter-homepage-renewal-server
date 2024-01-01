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
        Group found_group = groupRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return GroupResponseDto.builder()
                .group_id(found_group.getGroup_id())
                .member(found_group.getMember().getName())
                .profileImage(found_group.getMember().getProfileImage())
                .email(found_group.getMember().getEmail())
                .blogLink(found_group.getMember().getBlogLink())
                .gitRepositoryLink(found_group.getMember().getGitRepositoryLink())
                .role(found_group.getRole().getRole_name())
                .part(found_group.getPart())
                .year(found_group.getYear())
                .createdDate(found_group.getCreatedDate())
                .lastModifiedDate(found_group.getLastModifiedDate())
                .build();
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

        Group saved_group = groupRepository.save(group);
        return GroupResponseDto.builder()
                .group_id(saved_group.getGroup_id())
                .member(saved_group.getMember().getName())
                .profileImage(saved_group.getMember().getProfileImage())
                .email(saved_group.getMember().getEmail())
                .blogLink(saved_group.getMember().getBlogLink())
                .gitRepositoryLink(saved_group.getMember().getGitRepositoryLink())
                .role(saved_group.getRole().getRole_name())
                .part(saved_group.getPart())
                .year(saved_group.getYear())
                .createdDate(saved_group.getCreatedDate())
                .lastModifiedDate(saved_group.getLastModifiedDate())
                .build();
    }

    @Transactional
    public GroupResponseDto updateGroup(GroupRequestDto groupRequestDto, Long id) {
        // 여기서 외래키까지 다 변경할 수 있게 하려고 했는데, 과한듯
        // 그룹 객체 찾기
        Group foundGroup = groupRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        // 그룹 객체를 찾고서 내용을 변경한다.
        foundGroup.setGroup(id, groupRequestDto);
        // 변경된 내용을 저장소에 반영
        Group savedGroup = groupRepository.save(foundGroup);
        return GroupResponseDto.builder()
                .group_id(savedGroup.getGroup_id())
                .member(savedGroup.getMember().getName())
                .profileImage(savedGroup.getMember().getProfileImage())
                .email(savedGroup.getMember().getEmail())
                .blogLink(savedGroup.getMember().getBlogLink())
                .gitRepositoryLink(savedGroup.getMember().getGitRepositoryLink())
                .role(savedGroup.getRole().getRole_name())
                .part(savedGroup.getPart())
                .year(savedGroup.getYear())
                .createdDate(savedGroup.getCreatedDate())
                .lastModifiedDate(savedGroup.getLastModifiedDate())
                .build();
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
