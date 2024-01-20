package server.inuappcenter.kr.exception.customExceptions;

public class CustomPasswordMisMatchException extends RuntimeException {
    public CustomPasswordMisMatchException() {
        super("Incorrect password. Please try again.");
    }
}
