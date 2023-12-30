package server.inuappcenter.kr.controller;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.inuappcenter.kr.data.dto.response.SignInResponseDto;
import server.inuappcenter.kr.service.SignService;

@RestController
@RequestMapping("/sign")
@RequiredArgsConstructor
@Slf4j
public class SignController {
    private final SignService signService;
    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponseDto> signIn(
            @Parameter(name = "id", required = true) @RequestParam String id,
            @Parameter(name = "password", required = true) @RequestParam String password) throws RuntimeException {

        log.info("요청 들어옴" + id +  " " + password);
        SignInResponseDto signInResponseDto = signService.signIn(id, password);
        return ResponseEntity.status(HttpStatus.OK).body(signInResponseDto);
    }
}
