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
import server.inuappcenter.kr.data.dto.response.MemberResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.MemberRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private GroupRepository groupRepository;

    @InjectMocks
    private MemberService memberService;

    MemberRequestDto givenDto = new MemberRequestDto(
            "홍길동", "안녕하세요 제 이름은...", "https://...", "https://...",
            "test@test.com", "https:///..."
    );

    Member expectedEntity = new Member(new MemberRequestDto(
            "홍길동", "안녕하세요 제 이름은...", "https://...", "https://...",
            "test@test.com", "https://..."));

    MemberResponseDto expectedResult = MemberResponseDto.builder()
            .member_id(null)
            .email("test@test.com")
            .createdDate(null)
            .lastModifiedDate(null)
            .blogLink("https://...")
            .profileImage("https://...")
            .gitRepositoryLink("https://...")
            .description("안녕하세요 제 이름은...")
            .name("홍길동")
            .build();

    @DisplayName("동아리원 이름 가져오기 테스트")
    @Test
    public void getMemberTest() {
        // given
        Long givenId = 1L;
        given(memberRepository.findById(givenId)).willReturn(Optional.of(expectedEntity));
        // when
        MemberResponseDto result = memberService.getMember(givenId);
        // then
        assertEquals(expectedResult.getMember_id(), result.getMember_id());
    }

    @DisplayName("동아리원 이름 가져오기 실패 테스트")
    @Test
    public void getMemberFailTest() {
        // given
        Long givenId = 2L;
        given(memberRepository.findById(givenId)).willReturn(Optional.empty());
        // when, then
        assertThrows(CustomNotFoundException.class,() -> memberService.getMember(givenId));
    }

    @DisplayName("동아리원 저장 테스트")
    @Test
    public void saveMemberTest() {
        // given
        given(memberRepository.save(Mockito.any(Member.class))).willReturn(expectedEntity);
        // when
        MemberResponseDto result = memberService.saveMember(givenDto);
        // then
        assertEquals(expectedResult.getMember_id(), result.getMember_id());
        assertEquals(expectedResult.getCreatedDate(), result.getCreatedDate());
        assertEquals(expectedResult.getLastModifiedDate(), result.getLastModifiedDate());
        assertEquals(expectedResult.getName(), expectedEntity.getName());
        assertEquals(expectedResult.getDescription(), expectedEntity.getDescription());
        assertEquals(expectedResult.getBlogLink(), expectedEntity.getBlogLink());
        assertEquals(expectedResult.getEmail(), expectedEntity.getEmail());
    }

    @DisplayName("동아리원 수정 테스트")
    @Test
    public void updateMemberTest() {
        // given
        Long givenId = 1L;
        MemberRequestDto givenDto = new MemberRequestDto(
                "김길동", "안녕하세요 제 이름은...", "https://...", "https://...",
                "test@test.com", "https://...");
        given(memberRepository.findById(givenId)).willReturn(Optional.ofNullable(expectedEntity));
        expectedEntity.updateMember(givenId, givenDto);
        given(memberRepository.save(Mockito.any(Member.class))).willReturn(expectedEntity);
        // when
        MemberResponseDto result = memberService.updateMember(givenId,givenDto);
        // then
        assertEquals(givenDto.getName(), result.getName());
    }

    @DisplayName("동아리원 수정 실패 테스트")
    @Test
    public void updateMemberFailTest() {
        // given
        Long givenId = 1L;
        given(memberRepository.findById(givenId)).willReturn(Optional.empty());
        // when, then
        assertThrows(CustomNotFoundException.class, () -> memberService.updateMember(givenId, givenDto));
    }

    @DisplayName("모든 동아리원 목록 가져오기 테스트")
    @Test
    public void findAllMemberTest() {
        // given
        List<MemberResponseDto> expectedDtoList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            expectedDtoList.add(expectedResult);
        }
        List<Member> expectedEntityList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            expectedEntityList.add(expectedEntity);
        }
        given(memberRepository.findAll()).willReturn(expectedEntityList);
        // when
        List<MemberResponseDto> result = memberService.findAllMember();
        // then
        for (int i = 0; i < 10; i++) {
            assertEquals(expectedDtoList.get(i).getMember_id(), result.get(i).getMember_id());
            assertEquals(expectedDtoList.get(i).getName(), result.get(i).getName());
            assertEquals(expectedDtoList.get(i).getDescription(), result.get(i).getDescription());
            assertEquals(expectedDtoList.get(i).getProfileImage(), result.get(i).getProfileImage());
            assertEquals(expectedDtoList.get(i).getGitRepositoryLink(), result.get(i).getGitRepositoryLink());
            assertEquals(expectedDtoList.get(i).getBlogLink(), result.get(i).getBlogLink());
        }
    }

    @DisplayName("동아리원 삭제 실패 테스트")
    @Test
    public void deleteMemberFailTest() {
        // given
        Long givenId = 1L;
        given(memberRepository.findById(givenId)).willReturn(Optional.ofNullable(expectedEntity));
        ArrayList<Group> expectedGrupList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            expectedGrupList.add(new Group(expectedEntity, new Role(1L, "파트장"), new GroupRequestDto("서버", 14.4)));
        }
        given(groupRepository.findAllByMember(expectedEntity)).willReturn(expectedGrupList);
        CommonResponseDto expectedResult = new CommonResponseDto("The member [" + givenId + "] is part of a Group. Please delete the Group first");
        // when
        CommonResponseDto result = memberService.deleteMember(givenId);
        // then
        assertEquals(expectedResult.getMsg(), result.getMsg());
    }

    @DisplayName("동아리원 삭제 실패 테스트 (member가 없는 경우)")
    @Test
    public void deleteMemberNotFoundExceptionFailTest() {
        // given
        Long givenId = 1L;
        given(memberRepository.findById(givenId)).willReturn(Optional.empty());
        // when, then
        assertThrows(CustomNotFoundException.class, () -> memberService.deleteMember(givenId));
    }


    @DisplayName("동아리원 삭제 테스트")
    @Test
    public void deleteMemberTest() {
        // given
        Long givenId = 1L;
        given(memberRepository.findById(givenId)).willReturn(Optional.ofNullable(expectedEntity));
        ArrayList<Group> expectedGrupList = new ArrayList<>();
        given(groupRepository.findAllByMember(expectedEntity)).willReturn(expectedGrupList);
        CommonResponseDto expectedResult = new CommonResponseDto("member id ["+ givenId + "] has been deleted.");
        // when
        CommonResponseDto result = memberService.deleteMember(givenId);
        // then
        assertEquals(expectedResult.getMsg(), result.getMsg());
    }

    @DisplayName("동아리원 ID 이름으로 찾기 테스트")
    @Test
    public void findIdByNameTest() {
        // given
        String givenName = "홍길동";
        List<Member> expectedEntityList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expectedEntityList.add(expectedEntity);
        }
        List<MemberResponseDto> expectedDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expectedDtoList.add(expectedResult);
        }
        given(memberRepository.findAllByName(givenName)).willReturn(expectedEntityList);
        // when
        List<MemberResponseDto> result = memberService.findIdByName(givenName);
        // then
        for (int i = 0; i < 10; i++) {
            assertEquals(expectedDtoList.get(i).getMember_id(), result.get(i).getMember_id());
            assertEquals(expectedDtoList.get(i).getName(), result.get(i).getName());
            assertEquals(expectedDtoList.get(i).getDescription(), result.get(i).getDescription());
            assertEquals(expectedDtoList.get(i).getProfileImage(), result.get(i).getProfileImage());
            assertEquals(expectedDtoList.get(i).getGitRepositoryLink(), result.get(i).getGitRepositoryLink());
            assertEquals(expectedDtoList.get(i).getBlogLink(), result.get(i).getBlogLink());
        }
    }

}
