package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class NotFoundCommentException extends CustomRuntimeException {
    public NotFoundCommentException(){
        super("Not found comment");
    }
    public NotFoundCommentException(String msg){
        super(msg);
    }
}
