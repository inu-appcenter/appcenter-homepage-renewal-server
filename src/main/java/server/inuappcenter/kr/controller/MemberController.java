package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.dto.request.MemberRequestDto;
import server.inuappcenter.kr.data.dto.response.MemberResponseDto;
import server.inuappcenter.kr.service.MemberService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "동아리원 (1명) 정보 가져오기", description = "동아리원에게 부여된 id를 입력해주세요 / 동아리원(1명)을 반환합니다.")
    @Parameter(name = "id", description = "동아리원 id", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 Member를 요청했습니다.");
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @Operation(summary = "동아리원 (전체) 정보 가져오기", description = "전체 동아리원을 반환합니다.")
    @GetMapping("/all-members")
    public ResponseEntity<List<MemberResponseDto>> findAllMember() {
        log.info("사용자가 전체 Member 목록을 요청했습니다.");
        List<MemberResponseDto> dto_list = memberService.findAllMember();
        return ResponseEntity.status(HttpStatus.OK).body(dto_list);
    }

    @Operation(summary = "동아리원 (1명) 등록", description = "등록할 동아리원 정보를 입력해주세요")
    @PostMapping
    public ResponseEntity<MemberResponseDto> saveMember(final @RequestBody @Valid MemberRequestDto memberRequestDto) {
        log.info("사용자가 Member를 저장하도록 요청했습니다.\n" +
                 "MemberRequestDto의 내용: "+ memberRequestDto.toString());
        MemberResponseDto memberResponseDto = memberService.saveMember(memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponseDto);
    }

    @Operation(summary = "동아리원 (1명) 수정하기", description = "수정할 동아리원 정보를 입력해주세요")
    @Parameter(name = "id", description = "동아리원 id", required = true)
    @PatchMapping
    public ResponseEntity<MemberResponseDto> updateMember(@RequestBody MemberRequestDto memberRequestDto, final Long id) {
        log.info("사용자가 id: "+ id + "을(를) 가진 Member를 수정하도록 요청했습니다.\n" +
                "MemberRequestDto의 내용: "+ memberRequestDto.toString());
        MemberResponseDto memberResponseDto = memberService.updateMember(id, memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponseDto);
    }

    // 삭제 API 필요
    // 그룹에 등록되어 있는지 확인 후, 등록 안되어 있는 경우에만 삭제처리 하게끔 구현
    @Operation(summary = "동아리원 (1명) 삭제하기", description = "동아리원 삭제 / 동아리원이 그룹에 등록되어 있으면 삭제되지 않습니다.")
    @Parameter(name = "id", description = "동아리원 id", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteMember(final @PathVariable("id") Long id) {
        log.info("사용자가 id: " + id + "을(를) 가진 Member를 삭제하도록 요청했습니다.");
        CommonResponseDto result = memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
