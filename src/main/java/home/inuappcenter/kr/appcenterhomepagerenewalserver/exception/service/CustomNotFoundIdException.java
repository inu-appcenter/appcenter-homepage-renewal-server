package home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomNotFoundIdException extends RuntimeException {
    public CustomNotFoundIdException() {
        super("요청한 ID를 찾을 수 없습니다.");
        log.error("사용자가 요청한 ID를 찾을 수 없었습니다.");
    }
}
