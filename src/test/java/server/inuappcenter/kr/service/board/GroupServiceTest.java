package server.inuappcenter.kr.service.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
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
import server.inuappcenter.kr.service.GroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            "홍길동", "안녕하세요...", "https://...", "https://...", "test@test.com",
            "https://...")), new Role(new RoleRequestDto("파트장")), givenDto);
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

    @DisplayName("모든 그룹 가져오기 테스트")
    @Test
    public void findAllGroupTest() {
        // given
        for (int i = 0; i < 10; i++) {
            expectedResList.add(expectedResDto);
            expectedEntityList.add(givenEntity);
        }
        given(groupRepository.findAll()).willReturn(expectedEntityList);
        // when
        List<GroupResponseDto> result = groupService.findAllGroup();
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
                "홍길동", "안녕하세요...", "https://...", "https://...", "test@test.com",
                "https://..."));
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

    @DisplayName("그룹 내용 수정 테스트")
    @Test
    public void updateGroup() {
        // given
        given(groupRepository.findById(givenId)).willReturn(Optional.ofNullable(givenEntity));
        givenEntity.setGroup(givenId, givenDto);
        given(groupRepository.save(Mockito.any(Group.class))).willReturn(givenEntity);
        // when
        GroupResponseDto result = groupService.updateGroup(givenDto, givenId);
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
}