package home.inuappcenter.kr.appcenterhomepagerenewalserver.exception;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.customExceptions.CustomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Controller에서 발생하는 예외를 검출합니다.
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    // Controller에서 발생한 에러를 처리해줍니다.
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<?> notFoundIdException(CustomNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
