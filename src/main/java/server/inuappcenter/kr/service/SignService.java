package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import server.inuappcenter.kr.config.security.JwtTokenProvider;
import server.inuappcenter.kr.data.domain.User;
import server.inuappcenter.kr.data.dto.request.SignInRequestDto;
import server.inuappcenter.kr.data.dto.response.SignInResponseDto;
import server.inuappcenter.kr.data.repository.UserRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomPasswordMisMatchException;
import server.inuappcenter.kr.exception.customExceptions.CustomUsernameMisMatchException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignService {
    public final UserRepository userRepository;
    public final JwtTokenProvider jwtTokenProvider;

    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        User user = userRepository.getByUid(signInRequestDto.getId());
        if (user == null) {
            throw new CustomUsernameMisMatchException();
        }
        if (!signInRequestDto.getPassword().equals(user.getPassword())) {
            throw new CustomPasswordMisMatchException();
        }
        userRepository.save(user);
        return new SignInResponseDto(new String[]{
                jwtTokenProvider.createAccessToken(String.valueOf(user.getUid()))
        });
    }
}
