package com.board.portfolio.exception.custom;

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
