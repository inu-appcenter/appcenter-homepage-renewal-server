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
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.dto.request.MemberRequestDto;
import server.inuappcenter.kr.data.dto.response.MemberResponseDto;
import server.inuappcenter.kr.service.MemberService;

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

@WebMvcTest(MemberController.class)
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MemberService memberService;

    private final Long givenId = 1L;
    private final MemberRequestDto givenDto = new MemberRequestDto(
            "홍길동", "자기소개입니다.", "https://...", "https://...",
            "test@test.com", "https://...", "https://...", "010-1111-1111", "202102917",
            "컴퓨터공학부"
    );
    private final Member expectedEntity = new Member(givenDto);
    private final MemberResponseDto expectedDto = MemberResponseDto.entityToDto(expectedEntity);

    @WithMockUser
    @DisplayName("동아리원 한명 정보 테스트")
    @Test
    public void getMemberTest() throws Exception {
        // given
        given(memberService.getMember(givenId)).willReturn(expectedDto);
        // when
        mockMvc.perform(get("/members/" + givenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..member_id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.profileImage").exists())
                .andExpect(jsonPath("$.blogLink").exists())
                .andExpect(jsonPath("$..email").exists())
                .andExpect(jsonPath("$.gitRepositoryLink").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(memberService).getMember(givenId);
    }

    @WithMockUser
    @DisplayName("동아리원 전체 정보 가져오기 테스트")
    @Test
    public void findAllMemberTest() throws Exception {
        // given
        List<MemberResponseDto> expectedDtoList = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            expectedDtoList.add(expectedDto);
        }
        given(memberService.findAllMember()).willReturn(expectedDtoList);
        // when
        mockMvc.perform(get("/members/all-members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..member_id").exists())
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$..description").exists())
                .andExpect(jsonPath("$..profileImage").exists())
                .andExpect(jsonPath("$..blogLink").exists())
                .andExpect(jsonPath("$..email").exists())
                .andExpect(jsonPath("$..gitRepositoryLink").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(memberService).findAllMember();
    }

    @WithMockUser
    @DisplayName("동아리원 한명 등록 테스트")
    @Test
    public void saveMemberTest() throws Exception {
        // given
        given(memberService.saveMember(any(MemberRequestDto.class))).willReturn(expectedDto);
        String objectToJson = objectMapper.writeValueAsString(givenDto);
        // when
        mockMvc.perform(post("/members").content(objectToJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$..member_id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.profileImage").exists())
                .andExpect(jsonPath("$.blogLink").exists())
                .andExpect(jsonPath("$..email").exists())
                .andExpect(jsonPath("$.gitRepositoryLink").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(memberService).saveMember(any(MemberRequestDto.class));
    }

    @WithMockUser
    @DisplayName("동아리원 한명 수정하기 테스트")
    @Test
    public void updateMemberTest() throws Exception {
        // given
        given(memberService.updateMember(eq(givenId), any(MemberRequestDto.class))).willReturn(expectedDto);
        String objectToJson = objectMapper.writeValueAsString(givenDto);
        // when
        mockMvc.perform(patch("/members?id=" + givenId).content(objectToJson).content(objectToJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$..member_id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.profileImage").exists())
                .andExpect(jsonPath("$.blogLink").exists())
                .andExpect(jsonPath("$..email").exists())
                .andExpect(jsonPath("$.gitRepositoryLink").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(memberService).updateMember(eq(givenId), any(MemberRequestDto.class));
    }

    @WithMockUser
    @DisplayName("동아리원 한명 삭제하기 테스트")
    @Test
    public void deleteMemberTest() throws Exception {
        // given
        // when
        mockMvc.perform(delete("/members/" + givenId).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        verify(memberService).deleteMember(givenId);
    }

    @WithMockUser
    @DisplayName("동아리원 이름으로 ID 찾기 테스트")
    @Test
    public void findIdByName() throws Exception {
        // given
        String givenName = "홍길동";
        List<MemberResponseDto> expectedDtoList = new ArrayList<>();
        for (int i = 0; i < 3; i ++) {
            expectedDtoList.add(expectedDto);
        }
        given(memberService.findIdByName(givenName)).willReturn(expectedDtoList);
        // when
        mockMvc.perform(get("/members/id/" + givenName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..member_id").exists())
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$..description").exists())
                .andExpect(jsonPath("$..profileImage").exists())
                .andExpect(jsonPath("$..blogLink").exists())
                .andExpect(jsonPath("$..email").exists())
                .andExpect(jsonPath("$..gitRepositoryLink").exists())
                .andExpect(jsonPath("$..createdDate").exists())
                .andExpect(jsonPath("$..lastModifiedDate").exists())
                .andDo(print());
        // then
        verify(memberService).findIdByName(givenName);
    }

}
