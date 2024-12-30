package server.inuappcenter.kr.service.boardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.board.Board;
import server.inuappcenter.kr.data.domain.board.Image;
import server.inuappcenter.kr.data.dto.request.BoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.data.redis.repository.ImageRedisRepository;
import server.inuappcenter.kr.data.redis.repository.BoardResponseRedisRepository;
import server.inuappcenter.kr.data.repository.BoardRepository;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomFileSizeMisMatchException;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final BoardRepository<Board> boardRepository;
    private final ImageRepository imageRepository;
    private final HttpServletRequest request;
    private final BoardResponseRedisRepository<BoardResponseDto> boardResponseRedisRepository;
    private final ImageRedisRepository imageRedisRepository;

    @Transactional(readOnly = true)
    public BoardResponseDto findBoard(Long id) {
        return boardResponseRedisRepository.findById(id).orElseGet(
                () -> {
                    Board result = boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 보드가 없습니다."));
                    BoardResponseDto resultDto = result.createResponse(request);
                    boardResponseRedisRepository.save(resultDto);
                    return resultDto;
                }
        );
    }


    @Transactional
    public CommonResponseDto saveBoard(BoardRequestDto boardRequestDto) {
        Board savedBoard = boardRepository.save(boardRequestDto.createBoard());
        return new CommonResponseDto(savedBoard.getId() + " Board has been successfully saved.");
    }

    @Transactional
    public CommonResponseDto deleteBoard(Long id) {
        boardResponseRedisRepository.deleteById(id);
        boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 보드가 없습니다."));
        boardRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

    @Transactional
    public CommonResponseDto updateBoard(Long board_id, List<Long> image_id, BoardRequestDto boardRequestDto) {
        // 캐시에서 보드를 삭제한다.
        boardResponseRedisRepository.deleteById(board_id);

        Board foundBoard = boardRepository.findById(board_id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        List<MultipartFile> multipartFiles = boardRequestDto.getMultipartFiles();
        // 사용자가 multipart를 같이 보냈는지 확인
        if (multipartFiles != null || image_id != null) {

            if(image_id.size() != multipartFiles.size()) {
                throw new CustomFileSizeMisMatchException("photo_ids와 files의 크기가 다릅니다");
            }
            
            // 이미지 레포지토리에서 사용자가 보낸 ID로 조회를 먼저 진행하여, 찾아진 이미지 목록을 가짐
            List<Image> foundImageList = imageRepository.findByImageIdsAndBoard(image_id, foundBoard);

            // foundImageList에 있는 image의 id를 따로 뽑아서 저장
            List<Long> foundImageIds = new ArrayList<>();
            for (Image image : foundImageList) {
                // 찾아진 이미지 목록에서 id를 가져와 찾아진 id 목록에 추가함
                foundImageIds.add(image.getId());
            }
            // 캐시에서 이미지를 삭제한다.
            imageRedisRepository.deleteAllById(foundImageIds);

            // DB에 존재하지 않는 id(=새로 추가할 이미지의 id)를 찾음
            List<Long> missingImageIds = new ArrayList<>();
            for (Long id : image_id) {
                // 찾아진 Id 목록에 사용자가 보낸 ID가 존재하지 않는다면
                if (!foundImageIds.contains(id)) {
                    // 이 ID를 없는 이미지 ID에 추가함
                    missingImageIds.add(id);
                }
            }

            // 인덱스 맵핑 -> 수정과 추가가 섞일시 누락 발생에 대하여
            // image_id와 multipartFiles가 같은 인덱스로 매핑
            // image_id의 순서가 저장되어 있는 multipartFiles의 순서가 다를 수 있음.
            // 수정을 먼저 하고, 추가를 나중에 진행

            // 기존 이미지 인덱스 매핑 -> 이미지 수정이 함께 일어남.
            for (Image image : foundImageList) {
                Long currentImageId = image.getId();
                int idx = image_id.indexOf(currentImageId);
                if(idx != -1) {
                    MultipartFile updateFile = boardRequestDto.getMultipartFiles().get(idx);
                    image.updateImage(updateFile);
                }
            }

            // 새로운 이미지 리스트 생성
            List<Image> newImageList = new ArrayList<>();
            for(Long newImageId : missingImageIds) {
                int idx = image_id.indexOf(newImageId);
                if(idx != -1) {
                    MultipartFile newImageFile = boardRequestDto.getMultipartFiles().get(idx);
                    Image newImage = new Image(newImageFile);
                    newImageList.add(newImage);
                }
            }

            // 해당 board에 새로운 이미지 추가
            if (!newImageList.isEmpty()) {
                foundBoard.updateImage(newImageList);
            }

            imageRepository.saveAll(foundImageList);
            imageRepository.saveAll(newImageList);
        }
        // 이미지가 없을 경우 글 내용만 수정한다.
        foundBoard.modifyBoard(boardRequestDto);
        boardRepository.save(foundBoard);
        return new CommonResponseDto("id: " + board_id + " has been successfully modified.");
    }
}