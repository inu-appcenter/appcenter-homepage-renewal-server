package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.data.domain.RegistrationCode;
import server.inuappcenter.kr.data.domain.User;
import server.inuappcenter.kr.data.dto.request.RegistrationCodeRequestDto;
import server.inuappcenter.kr.data.dto.response.RegistrationCodeResponseDto;
import server.inuappcenter.kr.data.repository.RegistrationCodeRepository;
import server.inuappcenter.kr.data.repository.UserRepository;
import server.inuappcenter.kr.exception.customExceptions.CustomNotFoundException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationCodeService {
    private final RegistrationCodeRepository registrationCodeRepository;
    private final UserRepository userRepository;

    private void validateAdmin(String uid) {
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new CustomNotFoundException("사용자를 찾을 수 없습니다."));
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("관리자만 접근할 수 있습니다.");
        }
    }

    @Transactional
    public RegistrationCodeResponseDto saveOrUpdateCode(String uid, RegistrationCodeRequestDto requestDto) {
        validateAdmin(uid);
        Optional<RegistrationCode> existing = registrationCodeRepository.findTopByOrderByIdDesc();
        RegistrationCode code;
        if (existing.isPresent()) {
            code = existing.get();
            code.updateCode(requestDto.getCode());
        } else {
            code = new RegistrationCode(requestDto.getCode());
        }
        RegistrationCode saved = registrationCodeRepository.save(code);
        return RegistrationCodeResponseDto.entityToDto(saved);
    }

    @Transactional(readOnly = true)
    public RegistrationCodeResponseDto getCurrentCode(String uid) {
        validateAdmin(uid);
        RegistrationCode code = registrationCodeRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new CustomNotFoundException("등록된 인증코드가 없습니다."));
        return RegistrationCodeResponseDto.entityToDto(code);
    }

    @Transactional(readOnly = true)
    public void validateCode(String inputCode) {
        RegistrationCode code = registrationCodeRepository.findTopByOrderByIdDesc()
                .orElseThrow(() -> new IllegalArgumentException("등록된 인증코드가 없습니다. 관리자에게 문의하세요."));
        if (!code.getCode().equals(inputCode)) {
            throw new IllegalArgumentException("유효하지 않은 인증코드입니다.");
        }
    }
}
