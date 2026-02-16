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

    @Operation(summary = "회원가입", description = "새로운 멤버 계정을 등록합니다.<br/><br/>" +
            "<b>필수 항목:</b> 인증코드, 아이디(uid), 비밀번호, 이름, 이메일, 전화번호, 학번<br/>" +
            "<b>선택 항목:</b> 자기소개, 프로필 이미지, 블로그, Github, Behance, 학과<br/><br/>" +
            "이메일 또는 전화번호가 기존 멤버와 일치하면 해당 멤버에 자동 매칭됩니다.")
    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponseDto> signUp(final @RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(signService.signUp(signUpRequestDto));
    }

    @Operation(summary = "토큰 갱신", description = "리프레시 토큰으로 새로운 액세스 토큰을 발급받습니다.<br/>" +
            "액세스 토큰 만료 시 이 API를 호출하세요. 리프레시 토큰도 만료되면 다시 로그인해야 합니다.")
    @PostMapping("/refresh")
    public ResponseEntity<SignInResponseDto> refresh(final @RequestBody @Valid RefreshTokenRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).body(signService.refreshAccessToken(requestDto.getRefreshToken()));
    }

    @Operation(summary = "비밀번호 일괄 해싱", description = "평문으로 저장된 기존 비밀번호를 BCrypt로 일괄 해싱합니다. (관리자 전용, 1회성)")
    @PostMapping("/migrate-passwords")
    public ResponseEntity<CommonResponseDto> migratePasswords(
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(signService.migratePasswords(userDetails.getUsername()));
    }
}
