package home.inuappcenter.kr.appcenterhomepagerenewalserver.controller;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.GroupRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.ImageRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.BoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.BoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.GroupResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.service.IntroBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/introduction-board")
public class IntroductionBoardController {

    public final IntroBoardService introBoardService;

    @Operation(summary = "게시글 (1개) 가져오기", description = "가져올 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @GetMapping
    public ResponseEntity<BoardResponseDto<List<String>>> getBoard(Long id) {
        BoardResponseDto<List<String>> boardResponseDto = introBoardService.getBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }

    @Operation(summary = "게시글 (1개) 저장하기", description = "스웨거에서 작동하지 않는 액션 입니다. / 포스트맨을 사용해주세요")
    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<BoardResponseDto<List<Long>>> saveBoard(@RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                                                  @RequestPart(value = "introBoardRequestDto") BoardRequestDto boardRequestDto) throws IOException {

        ImageRequestDto imageRequestDto = new ImageRequestDto(multipartFileList);
        BoardResponseDto<List<Long>> boardResponseDto = introBoardService.saveBoard(boardRequestDto, imageRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }

    @Operation(summary = "게시글 (1개) 삭제하기", description = "삭제할 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @DeleteMapping
    public ResponseEntity<String> deleteBoard(Long id) {
        String result = introBoardService.deleteBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "앱 소개 글 (전체) 조회", description = "앱 소개 글을 모두 반환합니다.")
    @GetMapping("/all-boards-contents")
    public ResponseEntity<List<BoardResponseDto<String>>> findAllBoard() {
        List<BoardResponseDto<String>> dto_list = introBoardService.findAllBoard();
        return ResponseEntity.status(HttpStatus.OK).body(dto_list);
    }

    @Operation(summary = "게시글 (1개) 수정", description = "스웨거에서 작동하지 않는 액션 입니다. / 포스트맨을 사용해주세요")
    @Parameter(name = "board_id", description = "그룹 ID")
    @PatchMapping
    public ResponseEntity<BoardResponseDto<List<Long>>> updateBoard(@RequestPart(value = "introBoardRequestDto", required = false) BoardRequestDto boardRequestDto,
                                         @RequestPart(value ="board_id") Long board_id) throws Exception {
        BoardResponseDto<List<Long>> boardResponseDto = introBoardService.updateBoard(boardRequestDto, board_id);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }





}

