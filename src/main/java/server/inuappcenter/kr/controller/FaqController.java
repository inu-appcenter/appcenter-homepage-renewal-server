package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.data.dto.request.FaqBoardRequestDto;
import server.inuappcenter.kr.data.dto.response.FaqBoardResponseDto;
import server.inuappcenter.kr.service.BoardService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/faqs")
@Slf4j
public class FaqController {
    private final BoardService boardService;

    @Operation(summary = "FAQ 한 개 가져오기", description = "조회할 faq_id를 입력해주세요")
    @GetMapping("/{id}")
    public ResponseEntity<FaqBoardResponseDto> getFaq(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 FAQ를 요청했습니다.");
        FaqBoardResponseDto faqBoardResponseDto = boardService.getFaqBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(faqBoardResponseDto);
    }

    @Operation(summary = "FAQ 한 개 작성", description = "저장할 FAQ JSON을 보내주세요")
    @PostMapping
    public ResponseEntity<FaqBoardResponseDto> assignGroup(final @RequestBody @Valid FaqBoardRequestDto faqBoardRequestDto) {
        log.info("사용자가 FAQ를 저장하도록 요청했습니다.\n" +
                "FaqBoardRequestDto의 내용: "+ faqBoardRequestDto.toString());
        FaqBoardResponseDto faqBoardResponseDto = boardService.saveFaqBoard(faqBoardRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(faqBoardResponseDto);
    }

    @Operation(summary = "FAQ 한 개 수정", description = "수정할 FAQ JSON을 보내주세요")
    @PatchMapping
    public ResponseEntity<FaqBoardResponseDto> updateGroup(final @RequestBody @Valid FaqBoardRequestDto faqBoardRequestDto,
                                                        final Long id) {
        log.info("사용자가 id: "+ id + "을(를) 가진 FAQ를 수정하도록 요청했습니다.\n" +
                "FaqBoardRequestDto의 내용: "+ faqBoardRequestDto.toString());
        FaqBoardResponseDto faqBoardResponseDto = boardService.updateFaqBoard(id, faqBoardRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(faqBoardResponseDto);
    }

    @Operation(summary = "FAQ 한 개 삭제", description = "삭제할 faq_id를 입력해주세요")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 FAQ를 삭제하도록 요청했습니다.");
        String result = boardService.deleteFaqBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
