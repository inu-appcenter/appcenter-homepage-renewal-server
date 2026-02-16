package server.inuappcenter.kr.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.inuappcenter.kr.common.data.dto.CommonResponseDto;
import server.inuappcenter.kr.data.domain.EmailSubscription;
import server.inuappcenter.kr.data.dto.response.EmailSubscriptionResponseDto;
import server.inuappcenter.kr.data.repository.EmailSubscriptionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmailSubscriptionService {
    private final EmailSubscriptionRepository emailSubscriptionRepository;

    @Transactional
    public CommonResponseDto subscribe(String email) {
        if (emailSubscriptionRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        emailSubscriptionRepository.save(new EmailSubscription(email));
        return new CommonResponseDto("이메일이 등록되었습니다.");
    }

    @Transactional(readOnly = true)
    public List<EmailSubscriptionResponseDto> findAll() {
        return emailSubscriptionRepository.findAll().stream()
                .map(EmailSubscriptionResponseDto::entityToDto)
                .collect(Collectors.toList());
    }
}
