package home.inuappcenter.kr.appcenterhomepagerenewalserver.service;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Group;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Member;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.Role;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.GroupRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.GroupResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.GroupRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.MemberRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.repository.RoleRepository;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.customExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Transactional
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
                .build();
    }

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
                .build();
    }

    public String deleteGroup(Long id) {
        groupRepository.deleteById(id);
        return "id: " + id + " has been successfully deleted.";
    }
}
