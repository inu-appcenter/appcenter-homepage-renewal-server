package server.inuappcenter.kr.service;

import server.inuappcenter.kr.data.domain.board.*;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
import server.inuappcenter.kr.data.dto.response.IntroBoardResponseDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;
import server.inuappcenter.kr.data.repository.*;
import server.inuappcenter.kr.data.utils.BoardUtils;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository<Board> boardRepository;

    private final IntroBoardRepository introBoardRepository;
    private final PhotoBoardRepository photoBoardRepository;
    private final FaqRepository faqRepository;

    private final ImageRepository imageRepository;
    private final HttpServletRequest request;

    @Transactional
    // (앱) 게시글 조회하기
    public IntroBoardResponseDto<List<String>> getIntroBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        List<Image> ImageList = foundBoard.getImages();

        return new IntroBoardResponseDto<>(
                foundBoard.getId(),
                foundBoard.getTitle(),
                foundBoard.getSubTitle(),
                foundBoard.getAndroidStoreLink(),
                foundBoard.getIOSStoreLink(),
                foundBoard.getBody(),
                BoardUtils.returnImageURL(request, ImageList)
        );
    }

    @Transactional
    // (앱) 게시글 저장하기
    public IntroBoardResponseDto<List<Long>> saveIntroBoard(IntroBoardRequestDto introBoardRequestDto) {
        // IntroBoardRequestDTO 내용이 담긴 IntroBoard 타입의 인스턴스를 생성
        IntroBoard introBoard = new IntroBoard(introBoardRequestDto);
        // introBoard를 저장
        boardRepository.save(introBoard);

        // imageRequestDto에 포함된 List<MultiPartFile>을 List<Image>로 변환 / 매핑을 위해 introBoard도 포함하여 함께 저장
        List<Image> imageList = new <IntroBoard>Image().toImageListWithMapping(introBoardRequestDto.getMultipartFiles(), introBoard);
        List<Image> savedImage = imageRepository.saveAll(imageList);
        return new IntroBoardResponseDto<>(
                introBoard.getId(),
                introBoard.getTitle(),
                introBoard.getSubTitle(),
                introBoard.getAndroidStoreLink(),
                introBoard.getIOSStoreLink(),
                introBoard.getBody(),
                BoardUtils.returnImageId(savedImage)
        );
    }

    @Transactional
    public IntroBoardResponseDto<List<Long>> updateIntroBoard(IntroBoardRequestDto introBoardRequestDto, Long board_id) {
        IntroBoard foundBoard = introBoardRepository.findById(board_id).orElseThrow(()-> new CustomNotFoundException("The requested ID was not found."));
        foundBoard.updateBoard(introBoardRequestDto);
        List<Image> foundImg = imageRepository.findByIntroBoard(foundBoard);

        for(Image image: foundImg) {
            for(MultipartFile multipartFile: introBoardRequestDto.getMultipartFiles()) {
                image.setImage(multipartFile);
            }
        }

        IntroBoard introBoard = introBoardRepository.save(foundBoard);
        List<Image> savedImage = imageRepository.saveAll(foundImg);

        return new IntroBoardResponseDto<>(
                        introBoard.getId(),
                        introBoard.getTitle(),
                        introBoard.getSubTitle(),
                        introBoard.getAndroidStoreLink(),
                        introBoard.getIOSStoreLink(),
                        introBoard.getBody(),
                        BoardUtils.returnImageId(savedImage)
                        );
    }

    @Transactional
    public String deleteIntroBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow();

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        introBoardRepository.deleteById(id);

        return "id: " + id + " has been successfully deleted.";
    }

    @Transactional
    public List<IntroBoardResponseDto<String>> findAllIntroBoard() {
        List<IntroBoard> boardList = introBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return BoardUtils.returnIntroBoardResponseDtoList(boardList, thumbnailList, request);
    }

    @Transactional
    public PhotoBoardResponseDto<List<String>> getPhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        List<Image> ImageList = foundBoard.getImages();
        return new PhotoBoardResponseDto<>(
                foundBoard.getId(),
                foundBoard.getBody(),
                BoardUtils.returnImageURL(request, ImageList)
        );
    }

    @Transactional
    public PhotoBoardResponseDto<List<Long>> savePhotoBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        PhotoBoard photoBoard = new PhotoBoard(photoBoardRequestDto);
        List<Image> imageList = new <PhotoBoard>Image().toImageListWithMapping(photoBoardRequestDto.getMultipartFiles(), photoBoard);
        boardRepository.save(photoBoard);
        List<Image> savedImage = imageRepository.saveAll(imageList);
        return new PhotoBoardResponseDto<>(
                photoBoard.getId(),
                photoBoard.getBody(),
                BoardUtils.returnImageId(savedImage)
        );
    }

    @Transactional
    public PhotoBoardResponseDto<List<Long>> updatePhotoBoard(PhotoBoardRequestDto photoBoardRequestDto, Long board_id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(board_id).orElseThrow(()-> new CustomNotFoundException("The requested ID was not found."));
        foundBoard.updateBoard(photoBoardRequestDto);
        List<Image> foundImg = imageRepository.findByPhotoBoard(foundBoard);

        // 찾은 이미지에 내용 대입
        for(Image image: foundImg) {
            for(MultipartFile multipartFile: photoBoardRequestDto.getMultipartFiles()) {
                image.setImage(multipartFile);
            }
        }

        PhotoBoard photoBoard = photoBoardRepository.save(foundBoard);
        List<Image> savedImage = imageRepository.saveAll(foundImg);

        return new PhotoBoardResponseDto<>(
                photoBoard.getId(),
                photoBoard.getBody(),
                BoardUtils.returnImageId(savedImage)
        );
    }

    @Transactional
    public String deletePhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        boardRepository.deleteById(id);

        return "id: " + id + " has been successfully deleted.";
    }

    @Transactional
    public List<PhotoBoardResponseDto<String>> findAllPhotoBoard() {
        List<PhotoBoard> boardList = photoBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return BoardUtils.returnPhotoBoardResponseDtoList(boardList, thumbnailList, request);
    }

    @Transactional
    public FaqBoardResponseDto getFaqBoard(Long id) {
        FaqBoard foundBoard = faqRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return new FaqBoardResponseDto(
                foundBoard.getId(),
                foundBoard.getPart(),
                foundBoard.getQuestion(),
                foundBoard.getAnswer()
        );
    }

    @Transactional
    public FaqBoardResponseDto saveFaqBoard(FaqBoardRequestDto faqBoardRequestDto) {
        FaqBoard savedBoard = faqRepository.save(new FaqBoard(faqBoardRequestDto));
        return new FaqBoardResponseDto(
                savedBoard.getId(),
                savedBoard.getPart(),
                savedBoard.getQuestion(),
                savedBoard.getAnswer()
        );
    }

    @Transactional
    public FaqBoardResponseDto updateFaqBoard(Long id, FaqBoardRequestDto faqBoardRequestDto) {
        FaqBoard foundBoard = faqRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        foundBoard.updateFaqBoard(faqBoardRequestDto);
        return new FaqBoardResponseDto(
                foundBoard.getId(),
                foundBoard.getPart(),
                foundBoard.getQuestion(),
                foundBoard.getAnswer()
        );
    }

    @Transactional
    public String deleteFaqBoard(Long id) {
        faqRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        faqRepository.deleteById(id);
        return "id: " + id + " has been successfully deleted.";
    }
}
