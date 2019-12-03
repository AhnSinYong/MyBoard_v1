package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class AleadyAuthenticatedException extends CustomRuntimeException {
    public AleadyAuthenticatedException(){
        super("aleady authenticated account");
    }
    public AleadyAuthenticatedException(String msg){
        super(msg);
    }
}
