package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class FailSaveFileException extends CustomRuntimeException {
    public FailSaveFileException(){
        super("Fail to save file");
    }
    public FailSaveFileException(String msg){
        super(msg);
    }
}
