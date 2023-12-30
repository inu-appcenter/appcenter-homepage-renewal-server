package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.inuappcenter.kr.config.security.JwtTokenProvider;
import server.inuappcenter.kr.data.domain.User;
import server.inuappcenter.kr.data.dto.response.SignInResponseDto;
import server.inuappcenter.kr.data.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignService {
    public final UserRepository userRepository;
    public final JwtTokenProvider jwtTokenProvider;

    public SignInResponseDto signIn(String id, String password) {
        User user = userRepository.getByUid(id);
        log.info("비밀번호 판정 " + String.valueOf(password.equals(user.getPassword())));
        if (!password.equals(user.getPassword())) {
            throw new RuntimeException();
        }
        userRepository.save(user);
        return SignInResponseDto.builder()
                .success(true)
                .code(200)
                .msg("로그인에 성공하였습니다.")
                .token(new String[]{
                        jwtTokenProvider.createAccessToken(String.valueOf(user.getUid()))
                })
                .build();



    }
}
