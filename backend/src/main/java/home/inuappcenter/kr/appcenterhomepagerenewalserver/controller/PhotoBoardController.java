package home.inuappcenter.kr.appcenterhomepagerenewalserver.controller;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.domain.board.Image;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.BoardRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.ImageRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.BoardResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.service.PhotoBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/photo-board")
public class PhotoBoardController {
    private final PhotoBoardService photoBoardService;
    private final List<MultipartFile> multipartFileList;
    @Operation(summary = "게시글 (1개) 가져오기", description = "가져올 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @GetMapping
    public ResponseEntity<BoardResponseDto<List<String>>> getBoard(Long id) {
        BoardResponseDto<List<String>> boardResponseDto = photoBoardService.getBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }

    @Operation(summary = "게시글 (1개) 저장하기", description = "스웨거에서는 작동하지 않는 액션입니다.")
    @PostMapping(consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<BoardResponseDto<List<Long>>> saveBoard(@RequestPart(value = "multipartFileList", required = false) List<MultipartFile> multipartFileList,
                                                                  @RequestPart(value = "introBoardRequestDto") BoardRequestDto boardRequestDto) throws IOException {

        ImageRequestDto imageRequestDto = new ImageRequestDto(multipartFileList);
        BoardResponseDto<List<Long>> boardResponseDto = photoBoardService.saveBoard(boardRequestDto, imageRequestDto);

        return ResponseEntity.status(HttpStatus.OK).body(boardResponseDto);
    }

    @Operation(summary = "게시글 (1개) 삭제하기", description = "삭제할 게시글의 id를 입력해주세요")
    @Parameter(name = "id", description = "게시판 id")
    @DeleteMapping
    public ResponseEntity<String> deleteBoard(Long id) {
        String result = photoBoardService.deleteBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "사진 글 (전체) 조회", description = "사진 글을 모두 반환합니다.")
    @GetMapping("/all-boards-contents")
    public ResponseEntity<List<BoardResponseDto<String>>> findAllBoard() {
        List<BoardResponseDto<String>> dto_list = photoBoardService.findAllBoard();
        return ResponseEntity.status(HttpStatus.OK).body(dto_list);
    }

}

