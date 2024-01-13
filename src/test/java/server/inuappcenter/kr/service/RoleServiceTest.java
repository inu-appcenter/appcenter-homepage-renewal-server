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
import server.inuappcenter.kr.data.dto.response.RoleResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    GroupRepository groupRepository;

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

    @DisplayName("역할 가져오기 테스트")
    @Test
    public void getRoleTest() {
        // given
        Long givenId = 1L;
        Role exceptedEntity = new Role(new RoleRequestDto("파트장"));
        given(roleRepository.findById(givenId)).willReturn(Optional.of(exceptedEntity));
        RoleResponseDto exceptedResponse = RoleResponseDto.builder()
                .roleId(null)
                .roleName("파트장")
                .createdDate(null)
                .lastModifiedDate(null)
                .build();
        // when
        RoleResponseDto result = roleService.getRole(givenId);
        // then
        assertEquals(exceptedResponse.getRoleId(), result.getRoleId());
        assertEquals(exceptedResponse.getRoleName(), result.getRoleName());
        assertEquals(exceptedResponse.getCreatedDate(), result.getCreatedDate());
        assertEquals(exceptedResponse.getLastModifiedDate(), result.getLastModifiedDate());
    }

    @DisplayName("역할 저장 테스트")
    @Test
    public void getSaveRole() {
        // given
        RoleRequestDto givenDto = new RoleRequestDto("파트장");
        Role givenEntity = new Role(1L, "파트장");
        RoleResponseDto exceptedDto = RoleResponseDto.builder()
                .roleId(1L)
                .roleName("파트장")
                .createdDate(null)
                .lastModifiedDate(null).build();
        given(roleRepository.save(Mockito.any(Role.class))).willReturn(givenEntity);
        // when
        RoleResponseDto result = roleService.saveRole(givenDto);
        // then
        assertEquals(exceptedDto.getRoleId(), result.getRoleId());
        assertEquals(exceptedDto.getRoleName(), result.getRoleName());
        assertEquals(exceptedDto.getLastModifiedDate(), result.getLastModifiedDate());
        assertEquals(exceptedDto.getCreatedDate(), result.getCreatedDate());
    }

    @DisplayName("역할 수정 테스트")
    @Test
    public void updateRoleTest() {
        // given
        Long givenId = 1L;
        RoleRequestDto givenDto = new RoleRequestDto("센터장");
        Role givenEntity = new Role(1L, "파트장");
        given(roleRepository.findById(givenId)).willReturn(Optional.of(givenEntity));
        givenEntity.setRole(givenDto);
        Role givenEntityForSave = new Role(1L, "센터장");
        given(roleRepository.save(givenEntity)).willReturn(givenEntityForSave);
        RoleResponseDto exceptedResDto = RoleResponseDto.builder()
                .roleId(1L)
                .roleName("센터장")
                .createdDate(null)
                .lastModifiedDate(null)
                .build();

        // when
        RoleResponseDto result = roleService.updateRole(givenId, givenDto);
        // then
        assertEquals(exceptedResDto.getRoleId(), result.getRoleId());
        assertEquals(exceptedResDto.getRoleName(), result.getRoleName());
        assertEquals(exceptedResDto.getCreatedDate(), result.getCreatedDate());
        assertEquals(exceptedResDto.getLastModifiedDate(), result.getLastModifiedDate());
    }

    @DisplayName("역할 리스트 가져오기 테스트")
    @Test
    public void findAllRoleTest() {
        // given
        List<Role> resultEntity = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            resultEntity.add(new Role(Integer.toUnsignedLong(i), "파트장"));
        }
        List<RoleResponseDto> expectedDto = resultEntity.stream()
                .map(data -> data.toRoleResponseDto(data))
                .collect(Collectors.toList());
        given(roleRepository.findAll()).willReturn(resultEntity);
        // when
        List<RoleResponseDto> result = roleService.findAllRole();
        // then
        for (int i = 0; i < 10; i++) {
            assertEquals(expectedDto.get(i).getRoleId(), result.get(i).getRoleId());
            assertEquals(expectedDto.get(i).getCreatedDate(), result.get(i).getCreatedDate());
            assertEquals(expectedDto.get(i).getRoleName(), result.get(i).getRoleName());
            assertEquals(expectedDto.get(i).getLastModifiedDate(), result.get(i).getLastModifiedDate());
        }
    }

    @DisplayName("역할 삭제 실패 테스트")
    @Test
    public void deleteRoleTest() {
        // given
        Long givenId = 1L;
        ArrayList<Group> expectedEntity = new ArrayList<>();
        Role expectedRole = new Role(2L, "파트장");
        CommonResponseDto expectedResult = new CommonResponseDto("The role [" + givenId + "] is part of a Group. Please delete the Group first");
        for (int i = 1; i <= 10; i++) {
            expectedEntity.add(new Group(new Member(new MemberRequestDto(
                    "홍길동", "안녕하세요 저는....", "https://...", "https://...", "test@inu.ac.kr", "https://..."
            )), expectedRole, new GroupRequestDto("서버", 14.4)));
        }
        given(roleRepository.findById(givenId)).willReturn(Optional.of(expectedRole));
        given(groupRepository.findAllByRole(expectedRole)).willReturn(expectedEntity);
        // when
        CommonResponseDto result = roleService.deleteRole(givenId);
        // then
        assertEquals(expectedResult.getMsg(), result.getMsg());
    }

    @DisplayName("역할 삭제 테스트")
    @Test
    public void deleteRoleFailTest() {
        // given
        Long givenId = 1L;
        ArrayList<Group> expectedEntity = new ArrayList<>();
        Role expectedRole = new Role(3L, "파트장");
        CommonResponseDto expectedResult = new CommonResponseDto("role id [" + givenId + "] has been deleted.");
        given(roleRepository.findById(givenId)).willReturn(Optional.of(expectedRole));
        given(groupRepository.findAllByRole(expectedRole)).willReturn(expectedEntity);
        // when
        CommonResponseDto result = roleService.deleteRole(givenId);
        // then
        assertEquals(expectedResult.getMsg(), result.getMsg());
    }

    @DisplayName("역할 이름으로 ID 찾기 테스트")
    @Test
    public void findIdByNameTest() {
        // given
        String givenName = "파트장";
        List<Role> expectedRole = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            expectedRole.add(new Role(Integer.toUnsignedLong(i), "파트장"));
        }
        given(roleRepository.findAllByRoleName(givenName)).willReturn(expectedRole);
        List<RoleResponseDto> expectedResult = expectedRole.stream()
                .map(data -> data.toRoleResponseDto(data))
                .collect(Collectors.toList());
        // when
        List<RoleResponseDto> result = roleService.findIdByName(givenName);
        // then
        for (int i = 0; i < 10; i++){
            assertEquals(expectedResult.get(0).getRoleId(), result.get(0).getRoleId());
            assertEquals(expectedResult.get(0).getRoleName(), result.get(0).getRoleName());
            assertEquals(expectedResult.get(0).getCreatedDate(), result.get(0).getCreatedDate());
            assertEquals(expectedResult.get(0).getLastModifiedDate(), result.get(0).getLastModifiedDate());
        }
    }
}
