package kr.soft.autofeed.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.soft.autofeed.util.ResponseData;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseData> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ResponseData.error(400, ex.getMessage()));
    }
}

