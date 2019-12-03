package com.board.portfolio.controller.advice;

import com.board.portfolio.exception.CustomRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//TODO 단순 스트링 리턴에서 변경 필요
@Slf4j
@RestControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(BindException.class)
    public ResponseEntity validBindException(BindException e) {
        log.warn("@Valid bind 작동");
        return ResponseEntity.badRequest().body(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validException(MethodArgumentNotValidException e) {
        log.warn("@Valid 작동");
        return ResponseEntity.badRequest().body(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity customRuntimeException(CustomRuntimeException e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity runtimeException(RuntimeException e){
        log.error("runtimeException ", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
