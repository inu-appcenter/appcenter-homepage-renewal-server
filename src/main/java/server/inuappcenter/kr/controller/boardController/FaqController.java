package server.inuappcenter.kr.controller.boardController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.BoardResponseDto;
import server.inuappcenter.kr.service.boardService.AdditionalBoardService;
import server.inuappcenter.kr.service.boardService.BoardService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/faqs")
@Tag(name = "[FAQ] 질의응답 게시판")
@Slf4j
public class FaqController {
    private final BoardService boardService;
    private final AdditionalBoardService additionalBoardService;

    public FaqController(BoardService boardService, @Qualifier(value = "FaqBoardServiceImpl") AdditionalBoardService additionalBoardService) {
        this.boardService = boardService;
        this.additionalBoardService = additionalBoardService;
    }

    @Operation(summary = "FAQ 한 개 가져오기", description = "조회할 faq_id를 입력해주세요")
    @GetMapping("public/{id}")
    public ResponseEntity<BoardResponseDto> getFaqBoard(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.findBoard(id));
    }

    @Operation(summary = "FAQ 전체 가져오기", description = "전체 FAQ 목록을 가져옵니다.")
    @GetMapping("public/all-faq-boards")
    public ResponseEntity<List<BoardResponseDto>> getFaqBoardList() {
        return ResponseEntity.status(HttpStatus.OK).body(additionalBoardService.findBoardList());
    }

    @Operation(summary = "FAQ 한 개 작성", description = "저장할 FAQ JSON을 보내주세요")
    @PostMapping
    public ResponseEntity<CommonResponseDto> saveFaq(final @RequestBody @Valid FaqBoardRequestDto faqBoardRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(boardService.saveBoard(faqBoardRequestDto));
    }

    @Operation(summary = "FAQ 한 개 수정", description = "수정할 FAQ JSON을 보내주세요")
    @PatchMapping
    public ResponseEntity<CommonResponseDto> updateFaq(final @RequestBody @Valid FaqBoardRequestDto faqBoardRequestDto,
                                                        final Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.updateBoard(id, null, faqBoardRequestDto));
    }

    @Operation(summary = "FAQ 한 개 삭제", description = "삭제할 faq_id를 입력해주세요")
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteFaq(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(boardService.deleteBoard(id));
    }
}
