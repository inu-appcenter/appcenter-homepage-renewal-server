package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import java.util.Comparator;
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

    @Cacheable(value = "groupMembers", key = "(#year ?: 'all') + '_' + (#part ?: 'all')")
    @Transactional(readOnly = true)
    public List<GroupResponseDto> findAllGroup(Double year, String part) {
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
        return foundGroups.stream()
                .map(GroupResponseDto::entityToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "groupMembersInfo", key = "(#year ?: 'all') + '_' + (#part ?: 'all')")
    @Transactional(readOnly = true)
    public List<MemberWithGroupsResponseDto> findAllGroupWithDetails(Double year, String part) {
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
            projectsByMemberId.computeIfAbsent(memberId, k -> new ArrayList<>());
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
                    id -> new MemberWithGroupsResponseDto(member, new ArrayList<>(),
                            projectsByMemberId.getOrDefault(member.getId(), Collections.emptyList()))
            ).getGroups().add(MemberGroupEntryDto.from(group));
        }
        List<MemberWithGroupsResponseDto> result = new ArrayList<>(memberMap.values());

        // 각 멤버의 groups를 year 오름차순 정렬
        result.forEach(dto -> dto.getGroups().sort(
                Comparator.comparingDouble(MemberGroupEntryDto::getYear)
        ));

        // 멤버 목록을 가장 최근 year 기준 내림차순 정렬
        result.sort(Comparator.comparingDouble(
                (MemberWithGroupsResponseDto dto) -> dto.getGroups().stream()
                        .mapToDouble(MemberGroupEntryDto::getYear)
                        .max()
                        .orElse(0)
        ).reversed());

        return result;
    }

    @Caching(evict = {
            @CacheEvict(value = "groupMembers", allEntries = true),
            @CacheEvict(value = "groupMembersInfo", allEntries = true)
    })
    @Transactional
    public GroupResponseDto assignGroup(Long memberId, Long roleId, GroupRequestDto groupRequestDto) {
        Member found_member = memberRepository.findById(memberId).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        Role found_role = roleRepository.findById(roleId).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        Group group = new Group(found_member, found_role, groupRequestDto);

        Group savedGroup = groupRepository.save(group);
        return GroupResponseDto.entityToDto(savedGroup);
    }

    @Caching(evict = {
            @CacheEvict(value = "groupMembers", allEntries = true),
            @CacheEvict(value = "groupMembersInfo", allEntries = true)
    })
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

    @Caching(evict = {
            @CacheEvict(value = "groupMembers", allEntries = true),
            @CacheEvict(value = "groupMembersInfo", allEntries = true)
    })
    @Transactional
    public CommonResponseDto deleteGroup(Long id) {
        groupRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

    @Caching(evict = {
            @CacheEvict(value = "groupMembers", allEntries = true),
            @CacheEvict(value = "groupMembersInfo", allEntries = true)
    })
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
