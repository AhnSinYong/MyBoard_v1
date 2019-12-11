package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class NotFoundPostException extends CustomRuntimeException {
    public NotFoundPostException(){
        super("Not Found Post");
    }
    public NotFoundPostException(String msg){
        super(msg);
    }
}
