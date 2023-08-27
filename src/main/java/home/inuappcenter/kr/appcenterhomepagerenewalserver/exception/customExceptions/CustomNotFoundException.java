package home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.customExceptions;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomNotFoundException extends RuntimeException {
    public CustomNotFoundException(String message) {
        super(message);
    }
}
