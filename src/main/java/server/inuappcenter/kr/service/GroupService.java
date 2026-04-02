package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.IntroBoardGroup;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.Role;
import server.inuappcenter.kr.data.domain.board.IntroBoard;
import server.inuappcenter.kr.data.dto.request.GroupRequestDto;
import server.inuappcenter.kr.data.dto.response.GroupPartListResponseDto;
import server.inuappcenter.kr.data.dto.response.GroupResponseDto;
import server.inuappcenter.kr.data.dto.response.GroupYearListResponseDto;
import server.inuappcenter.kr.data.dto.response.MemberGroupEntryDto;
import server.inuappcenter.kr.data.dto.response.MemberProjectInfoDto;
import server.inuappcenter.kr.data.dto.response.MemberWithGroupsResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.IntroBoardGroupRepository;
import server.inuappcenter.kr.data.repository.MemberRepository;
import server.inuappcenter.kr.data.repository.RoleRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final IntroBoardGroupRepository introBoardGroupRepository;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final HttpServletRequest request;

@Transactional(readOnly = true)
    public GroupResponseDto getGroup(Long id) {
        Group foundGroup = groupRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return GroupResponseDto.entityToDto(foundGroup);
    }

    @Transactional(readOnly = true)
    public List<MemberWithGroupsResponseDto> findAllGroup(Double year, String part) {
        List<Group> foundGroups;
        if (year != null && part != null) {
            foundGroups = groupRepository.findAllByYearAndPartOrderByYear(year, part);
        } else if (year != null) {
            foundGroups = groupRepository.findAllByYearOrderByPart(year);
        } else if (part != null) {
            foundGroups = groupRepository.findAllByPartOrderByYearDesc(part);
        } else {
            foundGroups = groupRepository.findAll();
        }

        // 멤버 목록 추출 (중복 제거)
        Set<Long> seenMemberIds = new HashSet<>();
        List<Member> members = foundGroups.stream()
                .map(Group::getMember)
                .filter(m -> seenMemberIds.add(m.getId()))
                .collect(Collectors.toList());

        // 프로젝트 한 번에 배치 조회
        List<IntroBoardGroup> allIntroBoardGroups = introBoardGroupRepository.findAllByGroup_MemberIn(members);
        Map<Long, List<MemberProjectInfoDto>> projectsByMemberId = new HashMap<>();
        for (IntroBoardGroup ibg : allIntroBoardGroups) {
            Long memberId = ibg.getGroup().getMember().getId();
            projectsByMemberId.computeIfAbsent(memberId, k -> new java.util.ArrayList<>());
            IntroBoard ib = ibg.getIntroBoard();
            String mainImage = null;
            if (!ib.getImages().isEmpty()) {
                mainImage = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
                        + "/image/photo/" + ib.getImages().get(0).getId();
            }
            MemberProjectInfoDto projectDto = new MemberProjectInfoDto(ib.getId(), ib.getTitle(), mainImage);
            if (projectsByMemberId.get(memberId).stream().noneMatch(p -> p.getId().equals(ib.getId()))) {
                projectsByMemberId.get(memberId).add(projectDto);
            }
        }

        Map<Long, MemberWithGroupsResponseDto> memberMap = new LinkedHashMap<>();
        for (Group group : foundGroups) {
            Member member = group.getMember();
            memberMap.computeIfAbsent(member.getId(),
                    id -> new MemberWithGroupsResponseDto(member, new java.util.ArrayList<>(),
                            projectsByMemberId.getOrDefault(member.getId(), java.util.Collections.emptyList()))
            ).getGroups().add(MemberGroupEntryDto.from(group));
        }
        return new java.util.ArrayList<>(memberMap.values());
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
    public GroupResponseDto updateGroup(GroupRequestDto groupRequestDto, Long id, Long roleId) {
        Group foundGroup = groupRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        if (roleId != null) {
            Role foundRole = roleRepository.findById(roleId).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
            foundGroup.updateGroup(id, groupRequestDto, foundRole);
        }
        foundGroup.updateGroup(id, groupRequestDto, null);
        Group savedGroup = groupRepository.save(foundGroup);
        return GroupResponseDto.entityToDto(savedGroup);
    }

    @Transactional
    public CommonResponseDto deleteGroup(Long id) {
        groupRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

    @Transactional
    public CommonResponseDto deleteMultipleGroups(List<Long> id) {
        groupRepository.deleteAllByIdInBatch(id);
        return new CommonResponseDto("id: " + id + " have been successfully deleted.");
    }

    @Transactional(readOnly = true)
    public List<GroupResponseDto> searchByMemberName(String name) {
        List<Group> foundGroups = groupRepository.findAllByMember_Name(name);
        return foundGroups.stream()
                .map(data -> data.toGroupResponseDto(data))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GroupYearListResponseDto findAllYears() {
        List<Double> foundYears = groupRepository.findAllYearsDesc();
        return new GroupYearListResponseDto(foundYears);
    }

    @Transactional(readOnly = true)
    public GroupPartListResponseDto findAllParts() {
        List<String> foundParts = groupRepository.findAllParts();
        return new GroupPartListResponseDto(foundParts);
    }
}
