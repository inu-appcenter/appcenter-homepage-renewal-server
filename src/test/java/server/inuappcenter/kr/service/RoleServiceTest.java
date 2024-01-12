package server.inuappcenter.kr.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import server.inuappcenter.kr.data.domain.Role;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.RoleResponseDto;
import server.inuappcenter.kr.data.repository.GroupRepository;
import server.inuappcenter.kr.data.repository.RoleRepository;

import java.util.Optional;

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
}
