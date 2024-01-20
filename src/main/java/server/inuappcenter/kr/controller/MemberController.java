package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@Tag(name = "[Member] 동아리원 관리")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "동아리원 (1명) 정보 가져오기", description = "동아리원에게 부여된 id를 입력해주세요 / 동아리원(1명)을 반환합니다.")
    @Parameter(name = "id", description = "동아리원 id", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMember(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.getMember(id));
    }

    @Operation(summary = "동아리원 (전체) 정보 가져오기", description = "전체 동아리원을 반환합니다.")
    @GetMapping("/all-members")
    public ResponseEntity<List<MemberResponseDto>> findAllMember() {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findAllMember());
    }

    @Operation(summary = "동아리원 (1명) 등록", description = "등록할 동아리원 정보를 입력해주세요")
    @PostMapping
    public ResponseEntity<MemberResponseDto> saveMember(final @RequestBody @Valid MemberRequestDto memberRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.saveMember(memberRequestDto));
    }

    @Operation(summary = "동아리원 (1명) 수정하기", description = "수정할 동아리원 정보를 입력해주세요")
    @Parameter(name = "id", description = "동아리원 id", required = true)
    @PatchMapping
    public ResponseEntity<MemberResponseDto> updateMember(@RequestBody MemberRequestDto memberRequestDto, final Long id) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.updateMember(id, memberRequestDto));
    }

    // 삭제 API 필요
    // 그룹에 등록되어 있는지 확인 후, 등록 안되어 있는 경우에만 삭제처리 하게끔 구현
    @Operation(summary = "동아리원 (1명) 삭제하기", description = "동아리원 삭제 / 동아리원이 그룹에 등록되어 있으면 삭제되지 않습니다.")
    @Parameter(name = "id", description = "동아리원 id", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponseDto> deleteMember(final @PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.deleteMember(id));
    }

    @Operation(summary = "동아리원ID 이름으로 찾기", description = "동아리원 ID를 이름으로 찾을 수 있습니다.")
    @Parameter(name = "name", description = "동아리원 이름", required = true)
    @GetMapping("/id/{name}")
    public ResponseEntity<List<MemberResponseDto>> findIdByName(final @PathVariable("name") String name) {
        return ResponseEntity.status(HttpStatus.OK).body(memberService.findIdByName(name));
    }
}
