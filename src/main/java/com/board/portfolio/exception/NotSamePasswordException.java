package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class NotSamePasswordException extends CustomRuntimeException {
    public NotSamePasswordException(){
        super("Not same password and password check");
    }
    public NotSamePasswordException(String msg){
        super(msg);
    }
}
