package server.inuappcenter.kr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.RoleResponseDto;
import server.inuappcenter.kr.service.RoleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleController.class)
public class RoleControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RoleService roleService;

    private final Long givenId = 1L;
    private final RoleRequestDto givenDto = new RoleRequestDto("파트장");
    private final RoleResponseDto expectedDto = RoleResponseDto.builder()
            .roleId(givenId)
            .roleName("파트장")
            .createdDate(LocalDateTime.now())
            .lastModifiedDate(LocalDateTime.now())
            .build();

    @WithMockUser
    @DisplayName("동아리원 한명 정보 테스트")
    @Test
    public void getMemberTest() throws Exception {
        // given
        given(roleService.getRole(givenId)).willReturn(expectedDto);
        // when
        mockMvc.perform(get("/roles/" + givenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleId").exists())
                .andExpect(jsonPath("$.roleName").exists())
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(roleService).getRole(givenId);
    }

    @WithMockUser
    @DisplayName("동아리원 전체 정보 가져오기 테스트")
    @Test
    public void findAllMemberTest() throws Exception {
        // given
        List<RoleResponseDto> expectedDtoList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            expectedDtoList.add(expectedDto);
        }
        given(roleService.findAllRole()).willReturn(expectedDtoList);
        // when
        mockMvc.perform(get("/roles/all-roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..roleId").exists())
                .andExpect(jsonPath("$..roleName").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(roleService).findAllRole();
    }

    @WithMockUser
    @DisplayName("동아리원 한명 등록 테스트")
    @Test
    public void saveMemberTest() throws Exception {
        // given
        given(roleService.saveRole(any(RoleRequestDto.class))).willReturn(expectedDto);
        String objectToJson = objectMapper.writeValueAsString(givenDto);
        // when
        mockMvc.perform(post("/roles").content(objectToJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleId").exists())
                .andExpect(jsonPath("$.roleName").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(roleService).saveRole(any(RoleRequestDto.class));
    }

    @WithMockUser
    @DisplayName("동아리원 한명 수정하기 테스트")
    @Test
    public void updateMemberTest() throws Exception {
        // given
        given(roleService.updateRole(eq(givenId), any(RoleRequestDto.class))).willReturn(expectedDto);
        String objectToJson = objectMapper.writeValueAsString(givenDto);
        // when
        mockMvc.perform(patch("/roles?id=" + givenId).content(objectToJson).content(objectToJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleId").exists())
                .andExpect(jsonPath("$.roleName").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(roleService).updateRole(eq(givenId), any(RoleRequestDto.class));
    }

    @WithMockUser
    @DisplayName("동아리원 한명 삭제하기 테스트")
    @Test
    public void deleteMemberTest() throws Exception {
        // given
        // when
        mockMvc.perform(delete("/roles/" + givenId).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        verify(roleService).deleteRole(givenId);
    }

    @WithMockUser
    @DisplayName("동아리원 이름으로 ID 찾기 테스트")
    @Test
    public void findIdByName() throws Exception {
        // given
        String givenName = "홍길동";
        List<RoleResponseDto> expectedDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i ++) {
            expectedDtoList.add(expectedDto);
        }
        given(roleService.findIdByName(givenName)).willReturn(expectedDtoList);
        // when
        mockMvc.perform(get("/roles/id/" + givenName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..roleId").exists())
                .andExpect(jsonPath("$..roleName").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(roleService).findIdByName(givenName);
    }
}
