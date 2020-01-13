package com.board.portfolio.controller.advice;

import com.board.portfolio.exception.ApiError;
import com.board.portfolio.exception.ErrorContent;
import com.board.portfolio.exception.FieldErrorContent;
import com.board.portfolio.exception.GlobalErrorContent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest req) {
        List<ErrorContent> contentList = getErrorContentList(ex, BindException.class);
        return responseWithBody(ex, new ApiError(HttpStatus.BAD_REQUEST, contentList), req);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest req) {
        List<ErrorContent> contentList = getErrorContentList(ex, MethodArgumentNotValidException.class);
        return responseWithBody(ex,new ApiError(HttpStatus.BAD_REQUEST,contentList), req);
    }

    private ResponseEntity<Object> responseWithBody(Exception ex, ApiError body, WebRequest request){
        return super.handleExceptionInternal(ex, body, new HttpHeaders(), body.getStatus(), request);
    }

    private List<ErrorContent> getErrorContentList(Exception e,Class aClass){

        List<ErrorContent> contentList = new ArrayList<>();
        if(MethodArgumentNotValidException.class.isAssignableFrom(aClass)){
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            addFieldErrors(contentList, ex.getBindingResult().getFieldErrors());
            addGlobalErrors(contentList, ex.getBindingResult().getGlobalErrors());
        }
        else if(BindException.class.isAssignableFrom(aClass)){
            BindException ex = (BindException) e;
            addFieldErrors(contentList, ex.getBindingResult().getFieldErrors());
            addGlobalErrors(contentList, ex.getBindingResult().getGlobalErrors());
        }


        return contentList;
    }

    private void addFieldErrors(List<ErrorContent> contentList, List<FieldError> fieldErrorList){
        for(FieldError error : fieldErrorList){
            contentList.add(new FieldErrorContent(
                    error.getField(),
                    error.getDefaultMessage(),
                    error.getRejectedValue()
            ));
        }
    }

    private void addGlobalErrors(List<ErrorContent> contentList, List<ObjectError> globalErrorList){
        for(ObjectError error : globalErrorList){
            contentList.add(new GlobalErrorContent(
                    error.getDefaultMessage()
            ));
        }
    }

}
