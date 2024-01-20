package server.inuappcenter.kr.service.board;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.repository.PhotoBoardRepository;
import server.inuappcenter.kr.service.boardService.BoardService;
import server.inuappcenter.kr.service.boardService.impl.PhotoBoardServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PhotoBoardServiceImplTest {
    @Mock
    private BoardService boardService;
    @Mock
    private PhotoBoardRepository photoBoardRepository;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private HttpServletRequest request = new MockHttpServletRequest();
    @InjectMocks
    private PhotoBoardServiceImpl photoBoardServiceImpl;

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

    public List<Image> makeMockImageEntity() throws IOException {
        String imagePath = "test/image.jpg";
        ClassPathResource resource = new ClassPathResource(imagePath);
        Image expectedImage = new Image(resource.getFilename(), resource.getInputStream().readAllBytes(), 100L);
        expectedImage.updateIdForTest(givenId);
        List<Image> expectedImageList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            expectedImageList.add(expectedImage);
        }
        return expectedImageList;
    }

//    @DisplayName("사진 게시글 가져오기 테스트")
//    @Test
//    public void getPhotoBoardList() throws IOException {
//        // given
//        // givenBoard를 만든다 여기에는 mockMulitpartFile을 넣어준다.
//        PhotoBoardRequestDto givenBoardDto = new PhotoBoardRequestDto(
//                "글의 내용입니다.", makeMockMultipartFile());
//        // 메소드 실행결과로 예상되는 PhotoBoard를 만든다.
//        PhotoBoard expectedBoard = new PhotoBoard(givenBoardDto);
//        // PhotoBoard에 List<Image>를 넣어준다. 리스트는 가상 ID가 마킹된 Image 객체들로 구성되어있다.
//        expectedBoard.InjectImageListForTest(makeMockImageEntity());
//        given(boardService.getBoard(givenId)).willReturn(expectedBoard);
//        PhotoBoardResponseDto expectedResult = PhotoBoardResponseDto.entityToDto(request, expectedBoard);
//        // when
//        PhotoBoardResponseDto result = photoBoardService.getPhotoBoard(givenId);
//        // then
//        assertEquals(expectedResult.getBoard_id(), result.getBoard_id());
//        assertEquals(expectedResult.getBody(), result.getBody());
//        assertEquals(expectedResult.getImages(), result.getImages());
//    }

//    @DisplayName("사진 게시글 수정하기 테스트")
//    @Test
//    public void updatePhotoBoard() throws IOException {
//        // given
//        PhotoBoardRequestDto givenDto = new PhotoBoardRequestDto(
//                "수정하려는 내용", makeMockMultipartFile());
//        PhotoBoard expectedPhotoBoard = new PhotoBoard(givenDto);
//        expectedPhotoBoard.InjectImageListForTest(makeMockImageEntity());
//        List<Image> expectedImageList = makeMockImageEntity();
//        given(photoBoardRepository.findById(givenId)).willReturn(Optional.of(expectedPhotoBoard));
//        given(imageRepository.findByPhotoBoard(expectedPhotoBoard)).willReturn(expectedImageList);
//        given(photoBoardRepository.save(Mockito.any(PhotoBoard.class))).willReturn(expectedPhotoBoard);
//        given(imageRepository.saveAll(Mockito.anyList())).willReturn(expectedImageList);
//        PhotoBoardResponseDto expectedResult = PhotoBoardResponseDto.entityToDto(request, expectedPhotoBoard);
//        // when
//        PhotoBoardResponseDto result = photoBoardService.updatePhotoBoard(givenDto, givenId);
//        // then
//        assertEquals(expectedResult.getBoard_id(), result.getBoard_id());
//        assertEquals(expectedResult.getBody(), result.getBody());
//        assertEquals(expectedResult.getImages(), result.getImages());
//    }
//
//    @DisplayName("사진 게시글 수정 실패 테스트")
//    @Test
//    public void updatePhotoBoardFailTest() throws IOException {
//        // given
//        PhotoBoardRequestDto givenBoardDto = new PhotoBoardRequestDto(
//                "글의 내용입니다.", makeMockMultipartFile());
//        given(photoBoardRepository.findById(givenId)).willReturn(Optional.empty());
//        // when, then
//        assertThrows(CustomNotFoundException.class, () -> photoBoardService.updatePhotoBoard(givenBoardDto, givenId));
//    }

//    @DisplayName("모든 사진 게시글 가져오기 테스트")
//    @Test
//    public void findAllPhotoBoardTest() throws IOException {
//        // given
//        PhotoBoard givenPhotoBoard = new PhotoBoard(new PhotoBoardRequestDto(
//                "글의 내용입니다.", makeMockMultipartFile()
//        ));
//        givenPhotoBoard.InjectImageListForTest(makeMockImageEntity());
//        List<PhotoBoard> givenPhotoBoardList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            givenPhotoBoardList.add(givenPhotoBoard);
//        }
//        givenPhotoBoardList.get(0).getImages().get(0).isThumbnail();
//        List<Image> thumbnailList = new ArrayList<>();
//        for (PhotoBoard photoBoard: givenPhotoBoardList) {
//            for (int i = 0; i < photoBoard.getImages().size(); i++) {
//                if (photoBoard.getImages().get(i).getIsThumbnail()) {
//                    thumbnailList.add(photoBoard.getImages().get(i));
//                }
//            }
//        }
//        List<PhotoBoardResponseDto> expectedResult = BoardUtils.returnPhotoBoardResponseDtoList(givenPhotoBoardList, thumbnailList, request);
//        given(photoBoardRepository.findAll()).willReturn(givenPhotoBoardList);
//        // when
//        List<PhotoBoardResponseDto> result = photoBoardService.findAllPhotoBoard();
//        // then
//        for(int i = 0; i < 10; i++) {
//            assertEquals(expectedResult.get(i).getBoard_id(), result.get(i).getBoard_id());
//        }
//
//    }


}
