package server.inuappcenter.kr.service.board;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.repository.IntroBoardRepository;
import server.inuappcenter.kr.service.boardService.BoardService;
import server.inuappcenter.kr.service.boardService.impl.IntroBoardServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class IntroBoardServiceTest {
    @Mock
    private BoardService boardService;
    @Mock
    private IntroBoardRepository introBoardRepository;
    @Mock
    private ImageRepository imageRepository;
    @InjectMocks
    private IntroBoardServiceImpl introBoardService;

    @Mock
    private HttpServletRequest request = new MockHttpServletRequest();


    private final Long givenId = 1L;

    public List<MultipartFile> makeMockMultipartFile() throws IOException {
        String imagePath = "test/image.jpg";
        ClassPathResource resource = new ClassPathResource(imagePath);
        MultipartFile imageFile = new MockMultipartFile(
                "image",
                "test/image.jpg",
                "image/jpeg",
                resource.getInputStream().readAllBytes()
        );
        List<MultipartFile> imageFileList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            imageFileList.add(imageFile);
        }
        return imageFileList;
    }

//
//    @DisplayName("앱 게시글 가져오기 테스트")
//    @Test
//    public void getIntroBoardTest() throws IOException {
//        // given
//        String imagePath = "test/image.jpg";
//        ClassPathResource resource = new ClassPathResource(imagePath);
//        MultipartFile imageFile = new MockMultipartFile(
//                "image",
//                "test/image.jpg",
//                "image/jpeg",
//                resource.getInputStream().readAllBytes()
//        );
//        List<MultipartFile> imageFileList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            imageFileList.add(imageFile);
//        }
//        Image expectedImage = new Image(resource.getFilename(), resource.getInputStream().readAllBytes(), 100L);
//        expectedImage.updateIdForTest(givenId);
//        List<Image> expectedImageList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            expectedImageList.add(expectedImage);
//        }
//        expectedImage.updateIdForTest(givenId);
//        IntroBoard expectedBoard = new IntroBoard(new IntroBoardRequestDto(
//                "제목입니다.", "부제목입니다.", "https://...", "https://...", "내용입니다.",
//                imageFileList
//        ));
//        expectedBoard.InjectImageListForTest(expectedImageList);
//
//        IntroBoardResponseDto expectedResult = IntroBoardResponseDto.entityToDto(request, expectedBoard);
//        given(boardService.getBoard(givenId)).willReturn(expectedBoard);
//        // when
//        IntroBoardResponseDto result = introBoardService.getIntroBoard(givenId);
//        // then
//        assertEquals(expectedResult.getId(), result.getId());
//        assertEquals(expectedResult.getBody(), result.getBody());
//        assertEquals(expectedResult.getImages(), result.getImages());
//    }

//    @DisplayName("앱 게시글 수정 테스트")
//    @Test
//    public void updateIntroBoardTest() throws IOException {
//
//        String imagePath = "test/image.jpg";
//        ClassPathResource resource = new ClassPathResource(imagePath);
//        MultipartFile imageFile = new MockMultipartFile(
//                "image",
//                "test/image.jpg",
//                "image/jpeg",
//                resource.getInputStream().readAllBytes()
//        );
//        List<MultipartFile> imageFileList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            imageFileList.add(imageFile);
//        }
//        Image expectedImage = new Image(resource.getFilename(), resource.getInputStream().readAllBytes(), 100L);
//        expectedImage.updateIdForTest(givenId);
//        List<Image> expectedImageList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            expectedImageList.add(expectedImage);
//        }
//        expectedImage.setImage(imageFile);
//        IntroBoardRequestDto givenDto = new IntroBoardRequestDto(
//                "Test Application", "Example Todo-list Application", "https://...", "https://...", "변경된 내용",
//                imageFileList);
//        IntroBoard expectedBoard = new IntroBoard(givenDto);
//        expectedBoard.InjectImageListForTest(expectedImageList);
//        given(introBoardRepository.findById(givenId)).willReturn(Optional.of(expectedBoard));
//        given(imageRepository.findByIntroBoard(expectedBoard)).willReturn(expectedImageList);
//        given(introBoardRepository.save(Mockito.any(IntroBoard.class))).willReturn(expectedBoard);
//        given(imageRepository.saveAll(Mockito.anyList())).willReturn(expectedImageList);
//        IntroBoardResponseDto expectedResult = IntroBoardResponseDto.entityToDto(request, expectedBoard);
//        // when
//        IntroBoardResponseDto result = introBoardService.updateIntroBoard(givenDto, givenId);
//        // then
//        assertEquals(expectedResult.getId(), result.getId());
//        assertEquals(expectedResult.getBody(), result.getBody());
//        assertEquals(expectedResult.getImages(), result.getImages());
//    }
//
//    @DisplayName("앱 게시글 수정 실패 테스트")
//    @Test
//    public void updateIntroBoardFailTest() throws IOException {
//        // given
//        String imagePath = "test/image.jpg";
//        ClassPathResource resource = new ClassPathResource(imagePath);
//        MultipartFile imageFile = new MockMultipartFile(
//                "image",
//                "test/image.jpg",
//                "image/jpeg",
//                resource.getInputStream().readAllBytes()
//        );
//        List<MultipartFile> imageFileList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            imageFileList.add(imageFile);
//        }
//
//        IntroBoardRequestDto givenDto = new IntroBoardRequestDto(
//                "Test Application", "Example Todo-list Application", "https://...", "https://...", "변경된 내용",
//                imageFileList);
//        given(introBoardRepository.findById(givenId)).willReturn(Optional.empty());
//        // when, then
//        assertThrows(CustomNotFoundException.class, () -> introBoardService.updateIntroBoard(givenDto, givenId));
//    }

//    @DisplayName("앱 모든 게시글 가져오기 테스트")
//    @Test
//    public void findAllIntroBoardTest() throws IOException {
//        // given
//        String imagePath = "test/image.jpg";
//        ClassPathResource resource = new ClassPathResource(imagePath);
//        MultipartFile imageFile = new MockMultipartFile(
//                "image",
//                "test/image.jpg",
//                "image/jpeg",
//                resource.getInputStream().readAllBytes()
//        );
//        List<MultipartFile> imageFileList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            imageFileList.add(imageFile);
//        }
//        Image expectedImage = new Image(resource.getFilename(), resource.getInputStream().readAllBytes(), 100L);
//        expectedImage.updateIdForTest(givenId);
//        List<Image> expectedImageList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            expectedImageList.add(expectedImage);
//        }
//        expectedImage.setImage(imageFile);
//        IntroBoardRequestDto givenDto = new IntroBoardRequestDto(
//                "Test Application", "Example Todo-list Application", "https://...", "https://...", "This Application is...",
//                imageFileList);
//        IntroBoard expectedBoard = new IntroBoard(givenDto);
//        expectedBoard.InjectImageListForTest(expectedImageList);
//
//        List<IntroBoard> expectedIntroBoardList = new ArrayList<>();
//        for (int i = 0; i < 10; i++){
//            expectedIntroBoardList.add(expectedBoard);
//        }
//
//        expectedIntroBoardList.get(0).getImages().get(0).isThumbnail();
//
//        List<Image> thumbnailList = new ArrayList<>();
//        for (IntroBoard introBoard : expectedIntroBoardList) {
//            for (int j = 0; j < introBoard.getImages().size(); j++) {
//                if (introBoard.getImages().get(j).getIsThumbnail()) {
//                    thumbnailList.add(introBoard.getImages().get(j));
//                }
//            }
//        }
//
//        List<IntroBoardResponseDto> expectedResult = BoardUtils.returnIntroBoardResponseDtoList(expectedIntroBoardList, thumbnailList, request);
//        given(introBoardRepository.findAll()).willReturn(expectedIntroBoardList);
//        // when
//        List<IntroBoardResponseDto> result = introBoardService.getIntroBoardList();
//        // then
//        for(int i = 0; i < 10; i++) {
//            assertEquals(expectedResult.get(i).getId(), result.get(i).getId());
//        }
//
//    }
}
