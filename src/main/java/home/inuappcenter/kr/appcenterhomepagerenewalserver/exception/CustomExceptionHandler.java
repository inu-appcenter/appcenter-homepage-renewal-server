package home.inuappcenter.kr.appcenterhomepagerenewalserver.exception;

import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.customExceptions.CustomModelAttributeException;
import home.inuappcenter.kr.appcenterhomepagerenewalserver.exception.customExceptions.CustomNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

// Controller에서 발생하는 예외를 검출합니다.
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    // Controller에서 발생한 에러를 처리해줍니다.

    // Service에서 Id를 찾지 못했을 경우
    @ExceptionHandler(CustomNotFoundException.class)
    public ResponseEntity<String> notFoundException(CustomNotFoundException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // @Vaild 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> InvalidObjects(MethodArgumentNotValidException e) {
        log.error("서버로 전송된 데이터를 검증하는데 실패했습니다. 원인: " + Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("서버로 전송된 데이터를 검증하는데 실패했습니다.\n원인: " + Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler(CustomModelAttributeException.class)
    public ResponseEntity<String> modelAttributeException(CustomModelAttributeException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
