package com.board.portfolio.exception.custom;

import lombok.Getter;

@Getter
public class BlankPasswordException extends CustomRuntimeException {
    public BlankPasswordException(){
        super("Blank Password");
    }
    public BlankPasswordException(String msg){
        super(msg);
    }
}
