package server.inuappcenter.kr.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.controller.boardController.IntroBoardController;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;
import server.inuappcenter.kr.service.boardService.AdditionalBoardService;
import server.inuappcenter.kr.service.boardService.BoardService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IntroBoardController.class)
public class IntroBoardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BoardService boardService;
    @MockBean
    @Qualifier(value = "IntroBoardServiceImpl")
    private AdditionalBoardService additionalBoardService;


    public Map<Long, String> makeMockImageMap() {
        final Map<Long, String> mockImage = new HashMap<>();
        mockImage.put(1L, "https://...");
        return mockImage;
    }
    private final Long givenId = 1L;
    private final IntroBoardRequestDto givenDto = new IntroBoardRequestDto(
            "제목입니다.", "부제목입니다.", "https://...", "https://...",
            "내용입니다.", null
    );
    private final IntroBoardResponseDto expectedDto = IntroBoardResponseDto.builder()
            .id(givenId)
            .body("내용입니다.")
            .subTitle("부제목입니다.")
            .title("제목입니다.")
            .appleStoreLink("https://...")
            .androidStoreLink("https://...")
            .images(makeMockImageMap())
            .build();

    private final CommonResponseDto expectedCommonRes = new CommonResponseDto("id has been successfully modified.");

    @WithMockUser
    @DisplayName("IntroBoard 가져오기 테스트")
    @Test
    public void getBoardTest() throws Exception {
        // given
        given(boardService.findBoard(givenId)).willReturn(expectedDto);
        // when
        mockMvc.perform(get("/introduction-board/public/" + givenId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.subTitle").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.appleStoreLink").exists())
                .andExpect(jsonPath("$.androidStoreLink").exists())
                .andExpect(jsonPath("$.images").exists())
                .andDo(print());
        // then
        verify(boardService).findBoard(givenId);
    }

    @WithMockUser
    @DisplayName("IntroBoard 저장 테스트")
    @Test
    public void saveBoardTest() throws Exception {
        // given
        CommonResponseDto expectedResult = new CommonResponseDto(givenId + " Board has been successfully saved.");
        given(boardService.saveBoard(any(IntroBoardRequestDto.class))).willReturn(expectedResult);
        String imagePath = "test/image.jpg";
        ClassPathResource resource = new ClassPathResource(imagePath);

        MockMultipartFile file = new MockMultipartFile("multipartFiles", "filename1.jpg", "text/plain", resource.getInputStream().readAllBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("title", givenDto.getTitle());
        formData.add("subTitle", givenDto.getSubTitle());
        formData.add("appleStoreLink", givenDto.getAppleStoreLink());
        formData.add("androidStoreLink", givenDto.getAndroidStoreLink());
        formData.add("body", givenDto.getBody());

        // when
        mockMvc.perform(MockMvcRequestBuilders.multipart("/introduction-board")
                        .file(file)
                        .params(formData)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());

        // then
        verify(boardService).saveBoard(any(IntroBoardRequestDto.class));
    }

    @WithMockUser
    @DisplayName("IntroBoard 삭제 테스트")
    @Test
    public void deleteBoardTest() throws Exception {
        // given
        // when
        mockMvc.perform(delete("/introduction-board/"+givenId).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        verify(boardService).deleteBoard(givenId);
    }

    @WithMockUser
    @DisplayName("IntroBoard 목록 가져오기 테스트")
    @Test
    public void findAllBoardTest() throws Exception {
        // given
        List<BoardResponseDto> introBoardResponseDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            introBoardResponseDtoList.add(expectedDto);
        }
        given(additionalBoardService.findBoardList(null)).willReturn(introBoardResponseDtoList);
        // when
        mockMvc.perform(get("/introduction-board/public/all-boards-contents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..id").exists())
                .andExpect(jsonPath("$..subTitle").exists())
                .andExpect(jsonPath("$..title").exists())
                .andExpect(jsonPath("$..appleStoreLink").exists())
                .andExpect(jsonPath("$..androidStoreLink").exists())
                .andExpect(jsonPath("$..images").exists())
                .andDo(print());
        // then
        verify(additionalBoardService).findBoardList(null);
    }

    @WithMockUser
    @DisplayName("IntroBoard 수정 테스트")
    @Test
    public void updateBoardTest() throws Exception {
        List<Long> givenImageId = new ArrayList<>();
        givenImageId.add(1L);
        givenImageId.add(2L);
        // given
        given(boardService.updateBoard(eq(givenId), eq(givenImageId), any(IntroBoardRequestDto.class))).willReturn(expectedCommonRes);
        String imagePath = "test/image.jpg";
        ClassPathResource resource = new ClassPathResource(imagePath);

        MockMultipartFile file = new MockMultipartFile("multipartFiles", "filename1.jpg", "text/plain", resource.getInputStream().readAllBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("title", givenDto.getTitle());
        formData.add("subTitle", givenDto.getSubTitle());
        formData.add("appleStoreLink", givenDto.getAppleStoreLink());
        formData.add("androidStoreLink", givenDto.getAndroidStoreLink());
        formData.add("body", givenDto.getBody());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/introduction-board/1,2?board_id=1");
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });

        // when
        mockMvc.perform(builder
                        .file(file)
                        .params(formData)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        verify(boardService).updateBoard(eq(givenId), eq(givenImageId), any(IntroBoardRequestDto.class));
    }
}
