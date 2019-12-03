package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class InvalidAuthAccountException extends CustomRuntimeException {
    public InvalidAuthAccountException(){
        super("invalid account");
    }
    public InvalidAuthAccountException(String msg){
        super(msg);
    }
}
