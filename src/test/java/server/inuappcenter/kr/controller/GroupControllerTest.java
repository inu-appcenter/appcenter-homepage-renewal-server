package server.inuappcenter.kr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.Role;
import server.inuappcenter.kr.data.dto.request.GroupRequestDto;
import server.inuappcenter.kr.data.dto.request.MemberRequestDto;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.GroupResponseDto;
import server.inuappcenter.kr.service.GroupService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GroupController.class)
public class GroupControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private GroupService groupService;

    private final Long givenId = 1L;
    GroupRequestDto givenDto = new GroupRequestDto(
            "서버", 15.5
    );
    Group expectedEntity = new Group(new Member(new MemberRequestDto("홍길동", "자기소개입니다.", "https://...", "https://...", "test@test.com", "https://...")),
            new Role(new RoleRequestDto("파트장")), givenDto);
    GroupResponseDto expectedDto = GroupResponseDto.entityToDto(expectedEntity);
    @WithMockUser
    @DisplayName("그룹 멤버 한 명 조회 테스트")
    @Test
    public void getGroupTest() throws Exception {
        // given
        given(groupService.getGroup(givenId)).willReturn(expectedDto);
        // when
        mockMvc.perform(get("/groups/public/" + givenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..group_id").exists())
                .andExpect(jsonPath("$.member").exists())
                .andExpect(jsonPath("$.profileImage").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.blogLink").exists())
                .andExpect(jsonPath("$.gitRepositoryLink").exists())
                .andExpect(jsonPath("$.role").exists())
                .andExpect(jsonPath("$.part").exists())
                .andExpect(jsonPath("$.year").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(groupService).getGroup(givenId);
    }
}
