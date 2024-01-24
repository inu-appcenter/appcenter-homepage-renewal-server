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
import server.inuappcenter.kr.data.repository.BoardRepository;
import server.inuappcenter.kr.data.repository.ImageRepository;
import server.inuappcenter.kr.data.repository.redis.BoardResponseRedisRepository;
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
        boardRepository.findById(id).orElseThrow(() -> new CustomNotFoundException("ID에 해당되는 보드가 없습니다."));
        boardRepository.deleteById(id);
        return new CommonResponseDto("id: " + id + " has been successfully deleted.");
    }

    @Transactional
    public CommonResponseDto updateBoard(Long board_id, List<Long> image_id, BoardRequestDto boardRequestDto) {
        boardResponseRedisRepository.deleteById(board_id);
        Board foundBoard = boardRepository.findById(board_id).orElseThrow(() -> new CustomNotFoundException("The requested ID was not found."));
        // 사용자가 multipart를 같이 보냈는지 확인
        if (boardRequestDto.getMultipartFiles() != null || image_id != null) {
            // 이미지 레포지토리에서 사용자가 보낸 ID로 조회를 먼저 진행하여, 찾아진 이미지 목록을 가짐
            List<Image> foundImageList = imageRepository.findByImageIdsAndBoard(image_id, foundBoard);
            // multipart에서 이미지를 가져와 데이터와 정보를 해당 image에 업데이트
            for (Image image: foundImageList) {
                for (MultipartFile multipartFile: boardRequestDto.getMultipartFiles()) {
                    image.updateImage(multipartFile);
                }
            }
            // DB에 저장되지 않은 이미지에 대한 처리들을 진행해야 함
            // 먼저 DB에서 찾아진 ID에 대한 목록을 만들어줌
            List<Long> foundImageIds = new ArrayList<>();
            for (Image image : foundImageList) {
                // 찾아진 이미지 목록에서 id를 가져와 찾아진 id 목록에 추가함
                foundImageIds.add(image.getId());
            }
            // 찾아진 ID 목록에 존재하지 않는 ID를 얻어야 하기 때문에 없는 이미지 ID 목록을 만들어줌
            List<Long> missingImageIds = new ArrayList<>();
            for (Long id : image_id) {
                // 찾아진 Id 목록에 사용자가 보낸 ID가 존재하지 않는다면
                if (!foundImageIds.contains(id)) {
                    // 이 ID를 없는 이미지 ID에 추가함
                    missingImageIds.add(id);
                }
            }
            // 만약에 새로운 아이디(존재하지 않는 아이디)가 존재한다면
            if (!missingImageIds.isEmpty()) {
                // 존재하지 않는 ID 목록 수 만큼 새로운 이미지 객체를 만들어줌
                List<Image> newImageList = new ArrayList<>();
                for (int i = 0; i < missingImageIds.size(); i++) {
                    newImageList.add(new Image(boardRequestDto.getMultipartFiles().get(i)));
                }
                // 이 이미지 객체는 Board와 매핑되어 저장되어야 함
                // 따라서 새로운 이미지를 수정할 Board와 매핑시킨다.
                foundBoard.updateImage(newImageList);
            }

            // 변경된 이미지 정보를 저장
            imageRepository.saveAll(foundImageList);
        }
        // 이미지가 없을 경우 글 내용만 수정한다.
        foundBoard.modifyBoard(boardRequestDto);
        boardRepository.save(foundBoard);
        return new CommonResponseDto("id: " + board_id + " has been successfully modified.");
    }

}
