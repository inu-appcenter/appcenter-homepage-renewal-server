package home.inuappcenter.kr.appcenterhomepagerenewalserver.controller;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.ImageRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.IntroBoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.IntroBoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/introduction-board")
@Slf4j
public class IntroductionBoardController {

    public final BoardService boardService;

    @Operation(summary = "게시글 (1개) 가져오기", description = "가져올 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @GetMapping
    public ResponseEntity<IntroBoardResponseDto<List<String>>> getBoard(Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 IntroBoard를 요청했습니다.");
        IntroBoardResponseDto<List<String>> boardResponseDto = boardService.getIntroBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }

    @Operation(summary = "게시글 (1개) 저장하기", description = "스웨거에서 작동하지 않는 액션 입니다. / 포스트맨을 사용해주세요")
    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<IntroBoardResponseDto<List<Long>>> saveBoard(@RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                                                  @RequestPart(value = "introBoardRequestDto") IntroBoardRequestDto introBoardRequestDto) {
        log.info("사용자가 IntroBoard를 저장하도록 요청했습니다.\n" +
                "IntroBoardRequestDto의 내용: "+ introBoardRequestDto.toString());
        ImageRequestDto imageRequestDto = new ImageRequestDto(multipartFileList);
        IntroBoardResponseDto<List<Long>> introBoardResponseDto = boardService.saveIntroBoard(introBoardRequestDto, imageRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(introBoardResponseDto);
    }

    @Operation(summary = "게시글 (1개) 삭제하기", description = "삭제할 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @DeleteMapping
    public ResponseEntity<String> deleteBoard(Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 IntroBoard를 삭제하도록 요청했습니다.");
        String result = boardService.deleteIntroBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "앱 소개 글 (전체) 조회", description = "앱 소개 글을 모두 반환합니다.")
    @GetMapping("/all-boards-contents")
    public ResponseEntity<List<IntroBoardResponseDto<String>>> findAllBoard() {
        log.info("사용자가 전체 IntroBoard 목록을 요청했습니다.");
        List<IntroBoardResponseDto<String>> dto_list = boardService.findAllIntroBoard();
        return ResponseEntity.status(HttpStatus.OK).body(dto_list);
    }

    @Operation(summary = "게시글 (1개) 수정", description = "스웨거에서 작동하지 않는 액션 입니다. / 포스트맨을 사용해주세요")
    @Parameter(name = "board_id", description = "그룹 ID")
    @PatchMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<IntroBoardResponseDto<List<Long>>> updateBoard(
                                         @RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                         @RequestPart(value = "introBoardRequestDto") IntroBoardRequestDto introBoardRequestDto,
                                         @RequestPart(value ="board_id") Long id) {
        log.info("사용자가 id: "+ id + "을(를) 가진 IntroBoard를 수정하도록 요청했습니다.\n" +
                "IntroBoardRequestDto의 내용: "+ introBoardRequestDto.toString());
        ImageRequestDto imageRequestDto = new ImageRequestDto(multipartFileList);
        IntroBoardResponseDto<List<Long>> introBoardResponseDto = boardService.updateIntroBoard(introBoardRequestDto, imageRequestDto, id);
        return ResponseEntity.status(HttpStatus.OK).body(introBoardResponseDto);
    }

}

