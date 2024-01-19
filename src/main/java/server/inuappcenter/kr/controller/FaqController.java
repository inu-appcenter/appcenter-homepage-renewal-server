package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.service.boardService.BoardService;
import server.inuappcenter.kr.service.boardService.FaqBoardService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faqs")
@Slf4j
public class FaqController {
    private final BoardService boardService;
    private final FaqBoardService faqBoardService;

    @Operation(summary = "FAQ 한 개 가져오기", description = "조회할 faq_id를 입력해주세요")
    @GetMapping("public/{id}")
    public ResponseEntity<BoardResponseDto> getFaqBoard(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 FAQ를 요청했습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findBoard(id));
    }

    @Operation(summary = "FAQ 전체 가져오기", description = "전체 FAQ 목록을 가져옵니다.")
    @GetMapping("public/all-faq-boards")
    public ResponseEntity<List<BoardResponseDto>> getFaqBoardList() {
        log.info("사용자가 전체 FAQ 목록을 요청했습니다.");
        return ResponseEntity.status(HttpStatus.OK).body(faqBoardService.findFaqBoardList());
    }

    @Operation(summary = "FAQ 한 개 작성", description = "저장할 FAQ JSON을 보내주세요")
    @PostMapping
    public ResponseEntity<CommonResponseDto> saveFaq(final @RequestBody @Valid FaqBoardRequestDto faqBoardRequestDto) {
        log.info("사용자가 FAQ를 저장하도록 요청했습니다.\n" +
                "FaqBoardRequestDto의 내용: "+ faqBoardRequestDto.toString());
        CommonResponseDto commonResponseDto = boardService.saveBoard(faqBoardRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(commonResponseDto);
    }

    @Operation(summary = "FAQ 한 개 수정", description = "수정할 FAQ JSON을 보내주세요")
    @PatchMapping
    public ResponseEntity<CommonResponseDto> updateFaq(final @RequestBody @Valid FaqBoardRequestDto faqBoardRequestDto,
                                                        final Long id) {
        log.info("사용자가 id: "+ id + "을(를) 가진 FAQ를 수정하도록 요청했습니다.\n" +
                "FaqBoardRequestDto의 내용: "+ faqBoardRequestDto.toString());
        CommonResponseDto commonResponseDto = boardService.updateBoard(id, null, faqBoardRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(commonResponseDto);
    }

    @Operation(summary = "FAQ 한 개 삭제", description = "삭제할 faq_id를 입력해주세요")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteFaq(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 FAQ를 삭제하도록 요청했습니다.");
        CommonResponseDto result = boardService.deleteBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
