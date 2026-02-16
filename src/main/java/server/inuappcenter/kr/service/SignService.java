package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import server.inuappcenter.kr.config.security.JwtTokenProvider;
import server.inuappcenter.kr.data.domain.Member;
import server.inuappcenter.kr.data.domain.User;
import server.inuappcenter.kr.data.dto.request.SignInRequestDto;
import server.inuappcenter.kr.data.dto.request.SignUpRequestDto;
import server.inuappcenter.kr.data.dto.response.SignInResponseDto;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.RefreshToken;
import server.inuappcenter.kr.data.repository.MemberRepository;
import server.inuappcenter.kr.data.repository.RefreshTokenRepository;
import server.inuappcenter.kr.data.repository.UserRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;
import server.inuappcenter.kr.exception.customExceptions.CustomPasswordMisMatchException;
import server.inuappcenter.kr.exception.customExceptions.CustomUsernameMisMatchException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignService {
    public final UserRepository userRepository;
    public final MemberRepository memberRepository;
    public final RegistrationCodeService registrationCodeService;
    public final RefreshTokenRepository refreshTokenRepository;
    public final JwtTokenProvider jwtTokenProvider;
    public final PasswordEncoder passwordEncoder;

    @Transactional
    public SignInResponseDto signIn(SignInRequestDto signInRequestDto) {
        User user = userRepository.getByUid(signInRequestDto.getId());
        if (user == null) {
            throw new CustomUsernameMisMatchException();
        }

        boolean matched;
        if (user.getPassword().startsWith("$2")) {
            // BCrypt 해시된 비밀번호
            matched = passwordEncoder.matches(signInRequestDto.getPassword(), user.getPassword());
        } else {
            // 평문 비밀번호 → 비교 후 자동 해싱
            matched = signInRequestDto.getPassword().equals(user.getPassword());
            if (matched) {
                user.encodePassword(passwordEncoder.encode(signInRequestDto.getPassword()));
                userRepository.save(user);
                log.info("사용자 [{}]의 비밀번호가 자동 해싱 처리되었습니다.", user.getUid());
            }
        }

        if (!matched) {
            throw new CustomPasswordMisMatchException();
        }

        // 기존 리프레시 토큰 삭제 후 새로 발급
        refreshTokenRepository.deleteAllByUid(user.getUid());
        String accessToken = jwtTokenProvider.createAccessToken(user.getUid());
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUid());

        return new SignInResponseDto(accessToken, refreshToken);
    }

    @Transactional
    public SignInResponseDto refreshAccessToken(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

        if (refreshToken.isExpired()) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("리프레시 토큰이 만료되었습니다. 다시 로그인해주세요.");
        }

        String newAccessToken = jwtTokenProvider.createAccessToken(refreshToken.getUid());
        return new SignInResponseDto(newAccessToken, refreshToken.getToken());
    }

    public CommonResponseDto signUp(SignUpRequestDto signUpRequestDto) {
        // 최신 인증코드와 비교 검증
        registrationCodeService.validateCode(signUpRequestDto.getRegistrationCode());

        if (userRepository.getByUid(signUpRequestDto.getUid()) != null) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        // 이메일 또는 전화번호로 기존 Member 검색
        Member savedMember = findExistingMember(signUpRequestDto);

        // Member 먼저 생성 후 -> User 생성
        if (savedMember == null) {
            // 매칭되는 Member가 없으면 새로 생성
            Member member = new Member(signUpRequestDto.toMemberRequestDto());
            savedMember = memberRepository.save(member);
        }

        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());
        User user = new User(signUpRequestDto.getUid(), encodedPassword, savedMember);
        userRepository.save(user);

        return new CommonResponseDto("회원가입이 완료되었습니다. (uid: " + user.getUid() + ")");
    }

    @Transactional
    public CommonResponseDto migratePasswords(String uid) {
        User admin = userRepository.findByUid(uid)
                .orElseThrow(() -> new CustomNotFoundException("사용자를 찾을 수 없습니다."));
        if (!admin.isAdmin()) {
            throw new IllegalArgumentException("관리자만 접근할 수 있습니다.");
        }

        List<User> allUsers = userRepository.findAll();
        int count = 0;
        for (User u : allUsers) {
            // BCrypt 해시는 $2a$, $2b$, $2y$ 로 시작
            if (!u.getPassword().startsWith("$2")) {
                u.encodePassword(passwordEncoder.encode(u.getPassword()));
                userRepository.save(u);
                count++;
            }
        }
        return new CommonResponseDto(count + "개의 비밀번호가 해싱 처리되었습니다.");
    }

    private Member findExistingMember(SignUpRequestDto dto) {
        Member found = null;

        // 1. 이메일로 검색
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            found = memberRepository.findByEmail(dto.getEmail()).orElse(null);
        }

        // 2. 이메일로 못 찾으면 전화번호로 검색
        if (found == null && dto.getPhoneNumber() != null && !dto.getPhoneNumber().isBlank()) {
            found = memberRepository.findByPhoneNumber(dto.getPhoneNumber()).orElse(null);
        }

        if (found != null) {
            // 이미 다른 User와 연결된 Member인지 확인
            if (userRepository.findByMember(found).isPresent()) {
                throw new IllegalArgumentException("해당 멤버는 이미 계정이 존재합니다.");
            }
        }

        return found;
    }
}
