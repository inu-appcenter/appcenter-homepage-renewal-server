package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository<Board> boardRepository;

    private final IntroBoardRepository introBoardRepository;
    private final PhotoBoardRepository photoBoardRepository;
    private final FaqRepository faqRepository;

    private final ImageRepository imageRepository;
    private final HttpServletRequest request;

    @Transactional(readOnly = true)
    // (앱) 게시글 조회하기
    public IntroBoardResponseDto getIntroBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        return IntroBoardResponseDto.entityToDto(request, foundBoard);
    }

    @Transactional
    // (앱) 게시글 저장하기
    public CommonResponseDto saveIntroBoard(IntroBoardRequestDto introBoardRequestDto) {
        // imageRequestDto에 포함된 List<MultiPartFile>을 List<Image>로 변환 / 매핑을 위해 introBoard도 포함하여 함께 저장
        imageRepository.saveAll(Image.mappingPhotoAndEntity(introBoardRequestDto));
        return new CommonResponseDto("Board has been successfully saved.");
    }

    @Transactional
    public IntroBoardResponseDto updateIntroBoard(IntroBoardRequestDto introBoardRequestDto, Long board_id) {
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

        return IntroBoardResponseDto.entityToDto(request, introBoard);
    }

    @Transactional(readOnly = true)
    public CommonResponseDto deleteIntroBoard(Long id) {
        IntroBoard foundBoard = introBoardRepository.findById(id).orElseThrow();

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        introBoardRepository.deleteById(id);

        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

    @Transactional(readOnly = true)
    public List<IntroBoardResponseDto> findAllIntroBoard() {
        List<IntroBoard> boardList = introBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return BoardUtils.returnIntroBoardResponseDtoList(boardList, thumbnailList, request);
    }

    @Transactional(readOnly = true)
    public PhotoBoardResponseDto getPhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        return PhotoBoardResponseDto.entityToDto(request, foundBoard);
    }

    @Transactional
    public CommonResponseDto savePhotoBoard(PhotoBoardRequestDto photoBoardRequestDto) {
        photoBoardRepository.save(new PhotoBoard(photoBoardRequestDto));
        List<Image> imageList = Image.mappingPhotoAndEntity(photoBoardRequestDto);
        List<Image> savedImage = imageRepository.saveAll(imageList);
        return new CommonResponseDto(savedImage + "Board has been successfully saved.");
    }

    @Transactional
    public PhotoBoardResponseDto updatePhotoBoard(PhotoBoardRequestDto photoBoardRequestDto, Long board_id) {
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

        return PhotoBoardResponseDto.entityToDto(request, photoBoard);
    }

    @Transactional(readOnly = true)
    public CommonResponseDto deletePhotoBoard(Long id) {
        PhotoBoard foundBoard = photoBoardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));

        List<Image> ImageList = foundBoard.getImages();

        // 등록된 이미지 먼저 삭제
        for(Image image: ImageList) {
            imageRepository.deleteById(image.getId());
        }
        // 게시글 삭제
        boardRepository.deleteById(id);

        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

    @Transactional
    public List<PhotoBoardResponseDto> findAllPhotoBoard() {
        List<PhotoBoard> boardList = photoBoardRepository.findAll();

        List<Image> thumbnailList = imageRepository.findAllByIsThumbnailTrue();

        return BoardUtils.returnPhotoBoardResponseDtoList(boardList, thumbnailList, request);
    }

    @Transactional(readOnly = true)
    public FaqBoardResponseDto getFaqBoard(Long id) {
        FaqBoard foundBoard = faqRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        return FaqBoardResponseDto.entityToDto(foundBoard);
    }

    @Transactional(readOnly = true)
    public List<FaqBoardResponseDto> getFaqBoardList() {
        List<FaqBoard> boardList = faqRepository.findAll();
        return BoardUtils.returnFaqBoardResponseDtoList(boardList);
    }

    @Transactional
    public FaqBoardResponseDto saveFaqBoard(FaqBoardRequestDto faqBoardRequestDto) {
        FaqBoard savedBoard = faqRepository.save(new FaqBoard(faqBoardRequestDto));
        return FaqBoardResponseDto.entityToDto(savedBoard);
    }

    @Transactional
    public FaqBoardResponseDto updateFaqBoard(Long id, FaqBoardRequestDto faqBoardRequestDto) {
        FaqBoard foundBoard = faqRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        foundBoard.updateFaqBoard(faqBoardRequestDto);
        return FaqBoardResponseDto.entityToDto(foundBoard);
    }

    public CommonResponseDto deleteFaqBoard(Long id) {
        faqRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        faqRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }
}
