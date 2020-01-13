package com.board.portfolio.exception.custom;

import lombok.Getter;

@Getter
public class NotFoundFileException extends CustomRuntimeException {
    public NotFoundFileException(){
        super("Not Found File");
    }
    public NotFoundFileException(String msg){
        super(msg);
    }
}
