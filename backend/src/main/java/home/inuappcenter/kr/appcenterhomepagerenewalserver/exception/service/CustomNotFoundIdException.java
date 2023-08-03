package home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.service;

public class CustomNotFoundIdException extends RuntimeException{
    public CustomNotFoundIdException() {
        super("요청한 ID를 찾을 수 없습니다.");
    }
}
