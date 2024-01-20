package server.inuappcenter.kr.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.controller.boardController.FaqController;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
import server.inuappcenter.kr.service.boardService.BoardService;
import server.inuappcenter.kr.service.boardService.impl.FaqBoardServiceImpl;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FaqController.class)
public class FaqControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BoardService boardService;

    @MockBean
    FaqBoardServiceImpl faqBoardServiceImpl;

    private final Long givenId = 1L;
    FaqBoardRequestDto givenDto = new FaqBoardRequestDto("서버", "질문입니다.", "답변입니다.");
    FaqBoardResponseDto expectedDto = new FaqBoardResponseDto(
            1L, "서버", "질문입니다.", "답변입니다.", LocalDateTime.now(), LocalDateTime.now()
    );
    CommonResponseDto expectedCommonSaveResult = new CommonResponseDto( givenId + " Board has been successfully saved.");

    ResponseEntity<FaqBoardResponseDto> expectedResult = ResponseEntity.status(HttpStatus.OK).body(expectedDto);
//    @WithMockUser(username = "appcenter")
//    @DisplayName("FAQ 게시글 한개 가져오기 테스트")
//    @Test
//    public void getFaqBoardTest() throws Exception {
//        given(faqBoardService.getFaqBoard(givenId)).willReturn(expectedResult.getBody());
//        mockMvc.perform(
//                get("/faqs/public/" + givenId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.part").exists())
//                .andExpect(jsonPath("$.question").exists())
//                .andExpect(jsonPath("$.answer").exists())
//                .andExpect(jsonPath("$.createdDate").exists())
//                .andExpect(jsonPath("$.lastModifiedDate").exists())
//                .andDo(print());
//
//        verify(faqBoardService).getFaqBoard(givenId);
//    }
//
//    @WithMockUser(username = "appcenter")
//    @DisplayName("FAQ 전체 가져오기 테스트")
//    @Test
//    public void getFaqBoardListTest() throws Exception {
//        // given
//        List<FaqBoardResponseDto> expectedResponseList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            expectedResponseList.add(FaqBoardResponseDto.entityToDto(new FaqBoard(givenDto)));
//        }
//        given(faqBoardService.getFaqBoardList()).willReturn(expectedResponseList);
//        // when
//        mockMvc.perform(
//                get("/faqs/public/all-faq-boards"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$..id").exists())
//                .andExpect(jsonPath("$..part").exists())
//                .andExpect(jsonPath("$..question").exists())
//                .andExpect(jsonPath("$..answer").exists())
//                .andExpect(jsonPath("$..createdDate").exists())
//                .andExpect(jsonPath("$..lastModifiedDate").exists())
//                .andDo(print());
//        // then
//        verify(faqBoardService).getFaqBoardList();
//    }

    @WithMockUser(username = "appcenter")
    @DisplayName("FAQ 한 개 작성 테스트")
    @Test
    public void saveFaqTest() throws Exception {
        // given
        given(boardService.saveBoard(any(FaqBoardRequestDto.class))).willReturn(expectedCommonSaveResult);
        String givenJson = objectMapper.writeValueAsString(givenDto);
        // when
        mockMvc.perform(post("/faqs").content(givenJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());
        // then
        verify(boardService).saveBoard(any(FaqBoardRequestDto.class));
    }

//    @WithMockUser(username = "appcenter")
//    @DisplayName("FAQ 한 개 수정 테스트")
//    @Test
//    public void updateFaqTest() throws Exception {
//        // given
//        given(faqBoardService.updateFaqBoard(eq(givenId), any(FaqBoardRequestDto.class))).willReturn(expectedDto);
//        String givenJson = objectMapper.writeValueAsString(givenDto);
//        // when
//        mockMvc.perform(patch("/faqs?id="+ givenId).content(givenJson).contentType(MediaType.APPLICATION_JSON).with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").exists())
//                .andExpect(jsonPath("$.part").exists())
//                .andExpect(jsonPath("$.question").exists())
//                .andExpect(jsonPath("$.answer").exists())
//                .andExpect(jsonPath("$.createdDate").exists())
//                .andExpect(jsonPath("$.lastModifiedDate").exists())
//                .andDo(print());
//        // then
//        verify(faqBoardService).updateFaqBoard(eq(givenId), any(FaqBoardRequestDto.class));
//    }

    @WithMockUser(username = "appcenter")
    @DisplayName("FAQ 한 개 삭제 테스트")
    @Test
    public void deleteFaqTest() throws Exception {
        // given
        // when
        mockMvc.perform(delete("/faqs/" + givenId).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        verify(boardService).deleteBoard(givenId);
    }

}
