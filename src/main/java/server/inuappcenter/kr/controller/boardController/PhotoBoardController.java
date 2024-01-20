package server.inuappcenter.kr.controller.boardController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.PhotoBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.exception.customExceptions.CustomModelAttributeException;
import server.inuappcenter.kr.service.boardService.AdditionalBoardService;
import server.inuappcenter.kr.service.boardService.BoardService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/photo-board")
@Tag(name = "[Photo] 사진 게시판")
public class PhotoBoardController {
    private final BoardService boardService;
    private final AdditionalBoardService additionalBoardService;

    public PhotoBoardController(BoardService boardService, @Qualifier(value = "PhotoBoardServiceImpl") AdditionalBoardService additionalBoardService) {
        this.boardService = boardService;
        this.additionalBoardService = additionalBoardService;
    }

    @Operation(summary = "게시글 (1개) 가져오기", description = "가져올 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @GetMapping("/public/{id}")
    public ResponseEntity<BoardResponseDto> getBoard(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findBoard(id));
    }

    @Operation(summary = "게시글 (1개) 저장하기", description = "게시글을 저장합니다. (첫번째 사진은 게시글의 썸네일로 사용됩니다.)")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> saveBoard(final @ModelAttribute @Valid PhotoBoardRequestDto photoBoardRequestDto,
                                                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(boardService.saveBoard(photoBoardRequestDto));
        }
    }

    @Operation(summary = "게시글 수정", description = "사진 수정(추가)이 있을 경우에는 경로에 /{photo_ids}를 포함해주세요")
    @PatchMapping(path = {"/{photo_ids}", "/"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> updateBoard(
            final @PathVariable(name = "photo_ids", required = false) List<Long> photo_ids,
            final @ModelAttribute @Valid PhotoBoardRequestDto photoBoardRequestDto,
            BindingResult bindingResult,
            final @Parameter(name = "board_id") Long board_id) {
        if(bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(boardService.updateBoard(board_id, photo_ids, photoBoardRequestDto));
    }

    @Operation(summary = "게시글 (1개) 삭제하기", description = "삭제할 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteBoard(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.deleteBoard(id));
    }

    @Operation(summary = "사진 글 (전체) 조회", description = "사진 글을 모두 반환합니다.")
    @GetMapping("/public/all-boards-contents")
    public ResponseEntity<List<BoardResponseDto>> findAllBoard() {
        return ResponseEntity.status(HttpStatus.OK).body(additionalBoardService.findBoardList());
    }

}

