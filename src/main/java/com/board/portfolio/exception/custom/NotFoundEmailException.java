package com.board.portfolio.exception.custom;

import lombok.Getter;

@Getter
public class NotFoundEmailException extends CustomRuntimeException {
    public NotFoundEmailException(){
        super("Not Found Email");
    }
    public NotFoundEmailException(String msg){
        super(msg);
    }
}
