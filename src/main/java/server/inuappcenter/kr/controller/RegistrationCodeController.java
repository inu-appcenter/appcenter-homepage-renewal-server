package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.data.dto.request.RegistrationCodeRequestDto;
import server.inuappcenter.kr.data.dto.response.RegistrationCodeResponseDto;
import server.inuappcenter.kr.service.RegistrationCodeService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/registration-code")
@RequiredArgsConstructor
@Tag(name = "[Admin] 회원가입 인증코드 관리")
@PreAuthorize("hasRole('ADMIN')")
public class RegistrationCodeController {
    private final RegistrationCodeService registrationCodeService;

    @Operation(summary = "인증코드 설정/변경", description = "회원가입 인증코드를 설정하거나 변경합니다. 기존 코드가 있으면 업데이트됩니다. (관리자 전용)")
    @PutMapping
    public ResponseEntity<RegistrationCodeResponseDto> saveOrUpdateCode(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid RegistrationCodeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(registrationCodeService.saveOrUpdateCode(userDetails.getUsername(), requestDto));
    }

    @Operation(summary = "현재 인증코드 조회", description = "현재 설정된 인증코드를 조회합니다. (관리자 전용)")
    @GetMapping
    public ResponseEntity<RegistrationCodeResponseDto> getCurrentCode(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(registrationCodeService.getCurrentCode(userDetails.getUsername()));
    }
}
