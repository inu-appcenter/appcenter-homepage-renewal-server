package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.inuappcenter.kr.data.dto.request.SignInRequestDto;
import server.inuappcenter.kr.data.dto.response.SignInResponseDto;
import server.inuappcenter.kr.service.SignService;

@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
@Slf4j
public class SignController {
    private final SignService signService;
    @Operation(summary = "로그인", description = "지정된 관리자 계정으로 로그인해주세요")
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(final @RequestBody SignInRequestDto signInRequestDto) throws RuntimeException {

        SignInResponseDto signInResponseDto = signService.signIn(signInRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(signInResponseDto);
    }
}
