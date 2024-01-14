package server.inuappcenter.kr.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
import server.inuappcenter.kr.service.boardService.BoardService;
import server.inuappcenter.kr.service.boardService.FaqBoardService;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FaqController.class)
public class FaqControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    BoardService boardService;

    @MockBean
    FaqBoardService faqBoardService;

    private final Long givenId = 1L;
    FaqBoardResponseDto givenDto = new FaqBoardResponseDto(
            1L, "서버", "질문입니다.", "답변입니다.", LocalDateTime.now(), LocalDateTime.now()
    );

    ResponseEntity<FaqBoardResponseDto> expectedResult = ResponseEntity.status(HttpStatus.OK).body(givenDto);
    @WithMockUser(username = "appcenter")
    @DisplayName("FAQ 게시글 한개 가져오기")
    @Test
    public void getFaqBoardTest() throws Exception {
        given(faqBoardService.getFaqBoard(givenId)).willReturn(expectedResult.getBody());
        mockMvc.perform(
                get("/faqs/public/" + givenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.part").exists())
                .andExpect(jsonPath("$.question").exists())
                .andExpect(jsonPath("$.answer").exists())
                .andExpect(jsonPath("$.createdDate").exists())
                .andExpect(jsonPath("$.lastModifiedDate").exists())
                .andDo(print());

        verify(faqBoardService).getFaqBoard(givenId);
    }

}
