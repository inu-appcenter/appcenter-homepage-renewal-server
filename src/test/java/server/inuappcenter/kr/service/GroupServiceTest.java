package server.inuappcenter.kr.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.Role;
import server.inuappcenter.kr.data.dto.request.GroupRequestDto;
import server.inuappcenter.kr.data.dto.request.MemberRequestDto;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.GroupResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.MemberRepository;
import server.inuappcenter.kr.data.repository.RoleRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTest {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private GroupService groupService;

    GroupRequestDto givenDto = new GroupRequestDto("파트장", 14.5);
    Group givenEntity = new Group(new Member(new MemberRequestDto(
            "홍길동", "자기소개입니다.", "https://...", "https://...",
            "test@test.com", "https://...", "https://...", "010-1111-1111", "202102917",
            "컴퓨터공학부"
    )), new Role(new RoleRequestDto("파트장")), givenDto);
    Long givenId = 1L;
    GroupResponseDto expectedResDto = GroupResponseDto.entityToDto(givenEntity);
    List<GroupResponseDto> expectedResList = new ArrayList<>();
    List<Group> expectedEntityList = new ArrayList<>();
    @DisplayName("그룹 가져오기 테스트")
    @Test
    public void getGroupTest() {
        // given
        given(groupRepository.findById(givenId)).willReturn(Optional.ofNullable(givenEntity));
        GroupResponseDto expectedResult = GroupResponseDto.entityToDto(givenEntity);
        // when
        GroupResponseDto result = groupService.getGroup(givenId);
        // then
        assertEquals(expectedResult.getGroup_id(), result.getGroup_id());
        assertEquals(expectedResult.getRole(), result.getRole());
        assertEquals(expectedResult.getMember(), result.getMember());
    }

    @DisplayName("그룹 가져오기 실패 테스트")
    @Test
    public void getGroupFailTest() {
        // given
        given(groupRepository.findById(givenId)).willReturn(Optional.empty());
        // when, then
        assertThrows(CustomNotFoundException.class, () -> groupService.getGroup(givenId));
    }

    @DisplayName("모든 그룹 가져오기 테스트")
    @Test
    public void findAllGroupTest() {
        // given
        for (int i = 0; i < 10; i++) {
            expectedResList.add(expectedResDto);
            expectedEntityList.add(givenEntity);
        }
        given(groupRepository.findAllByYearAndPartOrderByYear(14.5, "Android")).willReturn(expectedEntityList);
        // when
        List<GroupResponseDto> result = groupService.findAllGroup(14.5, "Android");
        // then
        for (int i = 0; i < 10; i++) {
            assertEquals(expectedResList.get(i).getGroup_id(), result.get(i).getGroup_id());
            assertEquals(expectedResList.get(i).getMember(), result.get(i).getMember());
            assertEquals(expectedResList.get(i).getRole(), result.get(i).getRole());
        }
    }

    @DisplayName("그룹 저장 테스트")
    @Test
    public void assignGroupTest() {
        // given
        Member expectedMember = new Member(new MemberRequestDto(
                "홍길동", "자기소개입니다.", "https://...", "https://...",
                "test@test.com", "https://...", "https://...", "010-1111-1111", "202102917",
                "컴퓨터공학부"
        ));
        Role expectedRole = new Role(new RoleRequestDto("파트장"));
        given(memberRepository.findById(givenId)).willReturn(Optional.of(expectedMember));
        given(roleRepository.findById(givenId)).willReturn(Optional.of(expectedRole));
        given(groupRepository.save(Mockito.any(Group.class))).willReturn(givenEntity);
        // when
        GroupResponseDto result = groupService.assignGroup(givenId, givenId, givenDto);
        // then
        assertEquals(expectedResDto.getGroup_id(), result.getGroup_id());
        assertEquals(expectedResDto.getMember(), result.getMember());
        assertEquals(expectedResDto.getProfileImage(), result.getProfileImage());
        assertEquals(expectedResDto.getEmail(), result.getEmail());
        assertEquals(expectedResDto.getBlogLink(), result.getBlogLink());
        assertEquals(expectedResDto.getGitRepositoryLink(), result.getGitRepositoryLink());
        assertEquals(expectedResDto.getRole(), result.getRole());
        assertEquals(expectedResDto.getPart(), result.getPart());
        assertEquals(expectedResDto.getYear(), result.getYear());
        assertEquals(expectedResDto.getCreatedDate(), result.getCreatedDate());
        assertEquals(expectedResDto.getLastModifiedDate(), result.getLastModifiedDate());
    }

    @DisplayName("그룹 저장 실패 테스트")
    @Test
    public void assignGroupFailTest() {
        // given
        given(memberRepository.findById(givenId)).willReturn(Optional.empty());
        // when, then
        assertThrows(CustomNotFoundException.class, () -> groupService.assignGroup(givenId, givenId, givenDto));

        // case 2
        // given
        Member expectedMember = new Member(new MemberRequestDto(
                "홍길동", "자기소개입니다.", "https://...", "https://...",
                "test@test.com", "https://...", "https://...", "010-1111-1111", "202102917",
                "컴퓨터공학부"
        ));
        given(memberRepository.findById(givenId)).willReturn(Optional.of(expectedMember));
        given(roleRepository.findById(givenId)).willReturn(Optional.empty());
        // when, then
        assertThrows(CustomNotFoundException.class, () -> groupService.assignGroup(givenId, givenId, givenDto));
    }

    @DisplayName("그룹 내용 수정 테스트")
    @Test
    public void updateGroup() {
        // given
        given(groupRepository.findById(givenId)).willReturn(Optional.ofNullable(givenEntity));
        givenEntity.updateGroup(givenId, givenDto, null);
        given(groupRepository.save(Mockito.any(Group.class))).willReturn(givenEntity);
        // when
        GroupResponseDto result = groupService.updateGroup(givenDto, givenId, null);
        // then
        assertEquals(1L, result.getGroup_id());
        assertEquals(expectedResDto.getMember(), result.getMember());
        assertEquals(expectedResDto.getProfileImage(), result.getProfileImage());
        assertEquals(expectedResDto.getEmail(), result.getEmail());
        assertEquals(expectedResDto.getBlogLink(), result.getBlogLink());
        assertEquals(expectedResDto.getGitRepositoryLink(), result.getGitRepositoryLink());
        assertEquals(expectedResDto.getRole(), result.getRole());
        assertEquals(expectedResDto.getPart(), result.getPart());
        assertEquals(expectedResDto.getYear(), result.getYear());
        assertEquals(expectedResDto.getCreatedDate(), result.getCreatedDate());
        assertEquals(expectedResDto.getLastModifiedDate(), result.getLastModifiedDate());
    }

    @DisplayName("그룹 내용 수정 실패 테스트")
    @Test
    public void updateFailGroup() {
        // given
        given(groupRepository.findById(givenId)).willReturn(Optional.empty());
        // when, then
        assertThrows(CustomNotFoundException.class, () -> groupService.updateGroup(givenDto, givenId, null));
    }

    @DisplayName("그룹 삭제 테스트")
    @Test
    public void deleteGroupTest() {
        // given
        CommonResponseDto expectedDto = new CommonResponseDto("id: " + givenId + " has been successfully deleted.");
        // when
        CommonResponseDto result = groupService.deleteGroup(givenId);
        // then
        assertEquals(expectedDto.getMsg(), result.getMsg());
    }

    @DisplayName("여러 그룹 삭제 테스트")
    @Test
    public void deleteMultipleGroupsTest() {
        // given
        List<Long> givenLongList = new ArrayList<>();
        for(int i = 1; i <= 10; i++) {
            givenLongList.add((long) i);
        }
        CommonResponseDto expectedDto = new CommonResponseDto("id: " + givenLongList + " have been successfully deleted.");
        // when
        CommonResponseDto result = groupService.deleteMultipleGroups(givenLongList);
        // then
        assertEquals(expectedDto.getMsg(), result.getMsg());
    }

    @DisplayName("동아리원 이름으로 그룹 찾기 테스트")
    @Test
    public void searchByMemberNameTest() {
        // given
        String givenName = "홍길동";
        for (int i = 0; i < 10; i++) {
            expectedEntityList.add(givenEntity);
        }
        given(groupRepository.findAllByMember_Name(givenName)).willReturn(expectedEntityList);
        expectedResList = expectedEntityList.stream()
                .map(data -> data.toGroupResponseDto(data))
                .collect(Collectors.toList());
        // when
        List<GroupResponseDto> result = groupService.searchByMemberName(givenName);
        // then
        for (int i = 0; i < 10; i++) {
            assertEquals(expectedResList.get(i).getGroup_id(), result.get(i).getGroup_id());
            assertEquals(expectedResList.get(i).getMember(), result.get(i).getMember());
            assertEquals(expectedResList.get(i).getRole(), result.get(i).getRole());
        }
    }
}
