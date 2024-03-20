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
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.Group;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.Role;
import server.inuappcenter.kr.data.dto.request.GroupRequestDto;
import server.inuappcenter.kr.data.dto.request.MemberRequestDto;
import server.inuappcenter.kr.data.dto.request.RoleRequestDto;
import server.inuappcenter.kr.data.dto.response.GroupResponseDto;
import server.inuappcenter.kr.service.GroupService;

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
    Group expectedEntity = new Group(new Member(new MemberRequestDto(
            "홍길동", "자기소개입니다.", "https://...", "https://...",
            "test@test.com", "https://...", "https://...", "010-1111-1111", "202102917",
            "컴퓨터공학부"
    )),
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

//    @WithMockUser
//    @DisplayName("그룹 멤버 전체 조회 테스트")
//    @Test
//    public void findAllGroup() throws Exception {
//        // given
//        List<GroupResponseDto> expectedDtoList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            expectedDtoList.add(expectedDto);
//        }
//        given(groupService.findAllGroup()).willReturn(expectedDtoList);
//        // when
//        mockMvc.perform(get("/groups/public/all-groups-members"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$..group_id").exists())
//                .andExpect(jsonPath("$..member").exists())
//                .andExpect(jsonPath("$..profileImage").exists())
//                .andExpect(jsonPath("$..email").exists())
//                .andExpect(jsonPath("$..blogLink").exists())
//                .andExpect(jsonPath("$..gitRepositoryLink").exists())
//                .andExpect(jsonPath("$..role").exists())
//                .andExpect(jsonPath("$..part").exists())
//                .andExpect(jsonPath("$..year").exists())
//                .andExpect(jsonPath("$..createdDate").exists())
//                .andExpect(jsonPath("$..lastModifiedDate").exists())
//                .andDo(print());
//        // then
//        verify(groupService).findAllGroup();
//    }

    @WithMockUser
    @DisplayName("그룹 멤버 한 명 편성 테스트")
    @Test
    public void assignGroupTest() throws Exception {
        // given
        given(groupService.assignGroup(eq(givenId), eq(givenId), any(GroupRequestDto.class))).willReturn(expectedDto);
        String objectToJson = objectMapper.writeValueAsString(givenDto);
        // when
        mockMvc.perform(post("/groups?member_id=1&role_id=1").content(objectToJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isCreated())
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
        verify(groupService).assignGroup(eq(givenId), eq(givenId), any(GroupRequestDto.class));
    }

//    @WithMockUser
//    @DisplayName("그룹 멤버 한 명 수정 테스트")
//    @Test
//    public void updateGroupTest() throws Exception {
//        // given
//        given(groupService.updateGroup(any(GroupRequestDto.class), eq(givenId))).willReturn(expectedDto);
//        String objectToJson = objectMapper.writeValueAsString(givenDto);
//        // when
//        mockMvc.perform(patch("/groups?id="+givenId).content(objectToJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$..group_id").exists())
//                .andExpect(jsonPath("$.member").exists())
//                .andExpect(jsonPath("$.profileImage").exists())
//                .andExpect(jsonPath("$.email").exists())
//                .andExpect(jsonPath("$.blogLink").exists())
//                .andExpect(jsonPath("$.gitRepositoryLink").exists())
//                .andExpect(jsonPath("$.role").exists())
//                .andExpect(jsonPath("$.part").exists())
//                .andExpect(jsonPath("$.year").exists())
//                .andExpect(jsonPath("$..createdDate").exists())
//                .andExpect(jsonPath("$..lastModifiedDate").exists())
//                .andDo(print());
//        // then
//        verify(groupService).updateGroup(any(GroupRequestDto.class), eq(givenId));
//    }

    @WithMockUser
    @DisplayName("그룹 멤버 한 명 삭제 테스트")
    @Test
    public void deleteGroupsTest() throws Exception {
        // given
        // when
        mockMvc.perform(delete("/groups/"+givenId).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        verify(groupService).deleteGroup(givenId);
    }

    @WithMockUser
    @DisplayName("그룹 멤버 여러명 삭제 테스트")
    @Test
    public void deleteMultipleGroupsTest() throws Exception {
        // given
        List<Long> givenIdList = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            givenIdList.add(Integer.toUnsignedLong(i));
        }
        CommonResponseDto expectedResult = new CommonResponseDto("id: " + givenIdList + " have been successfully deleted.");
        given(groupService.deleteMultipleGroups(givenIdList)).willReturn(expectedResult);
        // when
        mockMvc.perform(delete("/groups/all-groups-members/1,2,3").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());
        // then
        verify(groupService).deleteMultipleGroups(givenIdList);
    }

    @WithMockUser
    @DisplayName("동아리원 이름으로 소속된 그룹들을 찾기")
    @Test
    public void searchByMemberName() throws Exception {
        // given
        List<GroupResponseDto> expectedDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expectedDtoList.add(expectedDto);
        }
        String givenName = "홍길동";
        given(groupService.searchByMemberName(givenName)).willReturn(expectedDtoList);
        // when
        mockMvc.perform(get("/groups/members/" + givenName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..group_id").exists())
                .andExpect(jsonPath("$..member").exists())
                .andExpect(jsonPath("$..profileImage").exists())
                .andExpect(jsonPath("$..email").exists())
                .andExpect(jsonPath("$..blogLink").exists())
                .andExpect(jsonPath("$..gitRepositoryLink").exists())
                .andExpect(jsonPath("$..role").exists())
                .andExpect(jsonPath("$..part").exists())
                .andExpect(jsonPath("$..year").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(groupService).searchByMemberName(givenName);
    }
}
