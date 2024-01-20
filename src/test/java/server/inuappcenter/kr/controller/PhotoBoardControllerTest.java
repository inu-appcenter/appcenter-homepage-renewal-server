package server.inuappcenter.kr.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.controller.boardController.PhotoBoardController;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;
import server.inuappcenter.kr.service.boardService.BoardService;
import server.inuappcenter.kr.service.boardService.impl.PhotoBoardServiceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PhotoBoardController.class)
public class PhotoBoardControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BoardService boardService;
    @MockBean
    private PhotoBoardServiceImpl photoBoardServiceImpl;


    public Map<Long, String> makeMockImageMap() {
        final Map<Long, String> mockImage = new HashMap<>();
        mockImage.put(1L, "https://...");
        return mockImage;
    }
    private final Long givenId = 1L;
    private final PhotoBoardRequestDto givenDto = new PhotoBoardRequestDto(
            "내용입니다.", null
    );
    private final PhotoBoardResponseDto expectedDto = PhotoBoardResponseDto.builder()
            .id(givenId)
            .body("내용입니다.")
            .createdDate(LocalDateTime.now())
            .lastModifiedDate(LocalDateTime.now())
            .images(makeMockImageMap())
            .build();

//    @WithMockUser
//    @DisplayName("PhotoBoard 가져오기 테스트")
//    @Test
//    public void getBoardTest() throws Exception {
//        // given
//        given(photoBoardService.getPhotoBoard(givenId)).willReturn(expectedDto);
//        // when
//        mockMvc.perform(get("/photo-board/public/" + givenId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.board_id").exists())
//                .andExpect(jsonPath("$.body").exists())
//                .andExpect(jsonPath("$.images").exists())
//                .andDo(print());
//        // then
//        verify(photoBoardService).getPhotoBoard(givenId);
//    }

    @WithMockUser
    @DisplayName("PhotoBoard 저장 테스트")
    @Test
    public void saveBoardTest() throws Exception {
        // given
        CommonResponseDto expectedResult = new CommonResponseDto(givenId + " Board has been successfully saved.");
        given(boardService.saveBoard(any(PhotoBoardRequestDto.class))).willReturn(expectedResult);
        String imagePath = "test/image.jpg";
        ClassPathResource resource = new ClassPathResource(imagePath);

        MockMultipartFile file = new MockMultipartFile("multipartFiles", "filename1.jpg", "text/plain", resource.getInputStream().readAllBytes());

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("body", givenDto.getBody());

        // when
        mockMvc.perform(MockMvcRequestBuilders.multipart("/photo-board")
                        .file(file)
                        .params(formData)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.msg").exists())
                .andDo(print());

        // then
        verify(boardService).saveBoard(any(PhotoBoardRequestDto.class));
    }

    @WithMockUser
    @DisplayName("PhotoBoard 삭제 테스트")
    @Test
    public void deleteBoardTest() throws Exception {
        // given
        // when
        mockMvc.perform(delete("/photo-board/"+givenId).with(csrf()))
                .andExpect(status().isOk())
                .andDo(print());
        // then
        verify(boardService).deleteBoard(givenId);
    }

//    @WithMockUser
//    @DisplayName("PhotoBoard 목록 가져오기 테스트")
//    @Test
//    public void findAllBoardTest() throws Exception {
//        // given
//        List<PhotoBoardResponseDto> introBoardResponseDtoList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            introBoardResponseDtoList.add(expectedDto);
//        }
//        given(photoBoardService.findAllPhotoBoard()).willReturn(introBoardResponseDtoList);
//        // when
//        mockMvc.perform(get("/photo-board/public/all-boards-contents"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$..board_id").exists())
//                .andExpect(jsonPath("$..body").exists())
//                .andExpect(jsonPath("$..images").exists())
//                .andDo(print());
//        // then
//        verify(photoBoardService).findAllPhotoBoard();
//    }

//    @WithMockUser
//    @DisplayName("PhotoBoard 수정 테스트")
//    @Test
//    public void updateBoardTest() throws Exception {
//        // given
//        given(photoBoardService.updatePhotoBoard(any(PhotoBoardRequestDto.class), eq(givenId))).willReturn(expectedDto);
//        String imagePath = "test/image.jpg";
//        ClassPathResource resource = new ClassPathResource(imagePath);
//
//        MockMultipartFile file = new MockMultipartFile("multipartFiles", "filename1.jpg", "text/plain", resource.getInputStream().readAllBytes());
//
//        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//        formData.add("body", givenDto.getBody());
//
//        MockMultipartHttpServletRequestBuilder builder =
//                MockMvcRequestBuilders.multipart("/photo-board?id=1");
//        builder.with(request -> {
//            request.setMethod("PATCH");
//            return request;
//        });
//
//        // when
//        mockMvc.perform(builder
//                        .file(file)
//                        .params(formData)
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.board_id").exists())
//                .andExpect(jsonPath("$.body").exists())
//                .andExpect(jsonPath("$.images").exists())
//                .andDo(print());
//
//        // then
//        verify(photoBoardService).updatePhotoBoard(any(PhotoBoardRequestDto.class), eq(givenId));
//    }
}
