package server.inuappcenter.kr.exception.customExceptions;

public class CustomUsernameMisMatchException extends RuntimeException{
    public CustomUsernameMisMatchException() {
        super("The ID does not exist.");
    }
}
