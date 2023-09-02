package server.inuappcenter.kr.controller;

import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.PhotoBoardResponseDto;
import server.inuappcenter.kr.exception.customExceptions.CustomModelAttributeException;
import server.inuappcenter.kr.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/photo-board")
@Slf4j
public class PhotoBoardController {
    private final BoardService boardService;

    @Operation(summary = "게시글 (1개) 가져오기", description = "가져올 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @GetMapping("/{id}")
    public ResponseEntity<PhotoBoardResponseDto<List<String>>> getBoard(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 PhotoBoard를 요청했습니다.");
        PhotoBoardResponseDto<List<String>> photoBoardResponseDto = boardService.getPhotoBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(photoBoardResponseDto);
    }

    @Operation(summary = "게시글 (1개) 저장하기", description = "스웨거에서는 작동하지 않는 액션입니다.")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PhotoBoardResponseDto<List<Long>>> saveBoard(final @ModelAttribute @Valid PhotoBoardRequestDto photoBoardRequestDto,
                                                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        } else {
            log.info("사용자가 PhotoBoard를 저장하도록 요청했습니다.\n" +
                    "PhotoBoardRequestDto의 내용: "+ photoBoardRequestDto.toString());
            PhotoBoardResponseDto<List<Long>> photoBoardResponseDto = boardService.savePhotoBoard(photoBoardRequestDto);
            return ResponseEntity.status(HttpStatus.OK).body(photoBoardResponseDto);
        }
    }

    @Operation(summary = "게시글 (1개) 수정")
    @PatchMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<PhotoBoardResponseDto<List<Long>>> updateBoard(
            final @ModelAttribute @Valid PhotoBoardRequestDto photoBoardRequestDto,
            BindingResult bindingResult,
            final @Parameter(name = "id", description = "그룹 ID", required = true) Long id) {

        if(bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        } else {
            log.info("사용자가 id: "+ id + "을(를) 가진 IntroBoard를 수정하도록 요청했습니다.\n" +
                    "IntroBoardRequestDto의 내용: "+ photoBoardRequestDto.toString());
            PhotoBoardResponseDto<List<Long>> introBoardResponseDto = boardService.updatePhotoBoard(photoBoardRequestDto, id);
            return ResponseEntity.status(HttpStatus.OK).body(introBoardResponseDto);
        }
    }

    @Operation(summary = "게시글 (1개) 삭제하기", description = "삭제할 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBoard(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 PhotoBoard를 삭제하도록 요청했습니다.");
        String result = boardService.deletePhotoBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "사진 글 (전체) 조회", description = "사진 글을 모두 반환합니다.")
    @GetMapping("/all-boards-contents")
    public ResponseEntity<List<PhotoBoardResponseDto<String>>> findAllBoard() {
        log.info("사용자가 전체 PhotoBoard 목록을 요청했습니다.");
        List<PhotoBoardResponseDto<String>> dto_list = boardService.findAllPhotoBoard();
        return ResponseEntity.status(HttpStatus.OK).body(dto_list);
    }

}

