package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.IntroBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.exception.customExceptions.CustomModelAttributeException;
import server.inuappcenter.kr.service.boardService.BoardService;
import server.inuappcenter.kr.service.boardService.IntroBoardService;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/introduction-board")
public class IntroBoardController {

    public final BoardService boardService;
    public final IntroBoardService introBoardService;

    @Operation(summary = "게시글 (1개) 가져오기", description = "가져올 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id", required = true)
    @GetMapping("/public/{id}")
    public ResponseEntity<BoardResponseDto> getBoard(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 IntroBoard를 요청했습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findBoard(id));
    }
    @Operation(summary = "게시글 (1개) 저장하기", description = "게시글을 저장합니다. (첫번째 사진은 게시글의 썸네일로 사용됩니다.)")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> saveBoard(final @ModelAttribute @Valid IntroBoardRequestDto introBoardRequestDto,
                                                                       BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        } else {
            log.info("사용자가 IntroBoard를 저장하도록 요청했습니다.\n" +
                    "IntroBoardRequestDto의 내용: "+ introBoardRequestDto.toString());
            return ResponseEntity.status(HttpStatus.CREATED).body(boardService.saveBoard(introBoardRequestDto));
        }
    }

    @Operation(summary = "게시글 (1개) 삭제하기", description = "삭제할 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteBoard(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 IntroBoard를 삭제하도록 요청했습니다.");
        CommonResponseDto result = boardService.deleteBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "앱 소개 글 (전체) 조회", description = "앱 소개 글을 모두 반환합니다.")
    @GetMapping("/public/all-boards-contents")
    public ResponseEntity<List<BoardResponseDto>> findAllBoard() {
        log.info("사용자가 전체 IntroBoard 목록을 요청했습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(introBoardService.findIntroBoardList());
    }


    @Operation(summary = "게시글 수정", description = "사진 수정(추가)이 있을 경우에는 경로에 /{photo_ids}를 포함해주세요")
    @PatchMapping(path = {"/{photo_ids}", "/"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponseDto> updateBoard(
            final @PathVariable(name = "photo_ids", required = false) List<Long> photo_ids,
            final @ModelAttribute @Valid IntroBoardRequestDto introBoardRequestDto,
            BindingResult bindingResult,
            final @Parameter(name = "board_id") Long board_id) {
        if(bindingResult.hasErrors()) {
            throw new CustomModelAttributeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        log.info("사용자가 id: "+ board_id + "을(를) 가진 IntroBoard를 수정하도록 요청했습니다.\n" +
                "IntroBoardRequestDto의 내용: "+ introBoardRequestDto.toString());
        CommonResponseDto commonResponseDto = boardService.updateBoard(board_id, photo_ids, introBoardRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponseDto);
    }

}

