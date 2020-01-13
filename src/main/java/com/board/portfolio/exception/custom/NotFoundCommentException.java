package com.board.portfolio.exception.custom;

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
