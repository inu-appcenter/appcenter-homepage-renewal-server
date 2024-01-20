package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.inuappcenter.kr.data.dto.request.SignInRequestDto;
import server.inuappcenter.kr.data.dto.response.SignInResponseDto;
import server.inuappcenter.kr.service.SignService;

@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
@Tag(name = "[Sign] 로그인")
public class SignController {
    private final SignService signService;

    @Operation(summary = "로그인", description = "지정된 관리자 계정으로 로그인해주세요")
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(final @RequestBody SignInRequestDto signInRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(signService.signIn(signInRequestDto));
    }
}
