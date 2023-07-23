package home.inuappcenter.kr.appcenterhomepagerenewalserver.controller;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.request.MemberRequestDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.data.dto.response.MemberResponseDto;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "동아리원 (1명) 정보 가져오기", description = "동아리원에게 부여된 id를 입력해주세요 / 동아리원(1명)을 반환합니다.")
    @Parameter(name = "id", description = "동아리원 id", required = true)
    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember(Long id) throws Exception {
        MemberResponseDto memberResponseDto = memberService.getMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(memberResponseDto);
    }

    @Operation(summary = "동아리원 (전체) 정보 가져오기", description = "전체 동아리원을 반환합니다.")
    @GetMapping("/all-members")
    public ResponseEntity<List<MemberResponseDto>> findAllMember() {
        List<MemberResponseDto> dto_list = memberService.findAllMember();
        return ResponseEntity.status(HttpStatus.OK).body(dto_list);
    }

    @Operation(summary = "동아리원 (1명) 등록하기", description = "등록할 동아리원 정보를 입력해주세요")
    @PostMapping
    public ResponseEntity<MemberResponseDto> saveMember(@RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.saveMember(memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponseDto);
    }

    @Operation(summary = "동아리원 (1명) 수정하기", description = "수정할 동아리원 정보를 입력해주세요")
    @Parameter(name = "id", description = "동아리원 id")
    @PatchMapping
    public ResponseEntity<MemberResponseDto> updateMember(@RequestBody MemberRequestDto memberRequestDto, Long id) throws Exception {
        MemberResponseDto memberResponseDto = memberService.updateMember(id, memberRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(memberResponseDto);
    }

    // 삭제 API 필요
    // 그룹에 등록되어 있는지 확인 후, 등록 안되어 있는 경우에만 삭제처리 하게끔 구현
    @Operation(summary = "동아리원 (1명) 삭제하기", description = "동아리원 삭제 / 동아리원이 그룹에 등록되어 있으면 삭제되지 않습니다.")
    @Parameter(name = "id", description = "동아리원 id")
    @DeleteMapping
    public ResponseEntity<String> deleteMember(Long id) throws Exception {
        String result = memberService.deleteMember(id);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
