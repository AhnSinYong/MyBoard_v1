package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class NotAllowAccessException extends CustomRuntimeException {
    public NotAllowAccessException(){
        super("Not allow access");
    }
    public NotAllowAccessException(String msg){
        super(msg);
    }
}
