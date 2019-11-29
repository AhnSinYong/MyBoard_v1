package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class BlankEmailException extends CustomRuntimeException {
    public BlankEmailException(){
        super("Blank Email");
    }
    public BlankEmailException(String msg){
        super(msg);
    }
}
