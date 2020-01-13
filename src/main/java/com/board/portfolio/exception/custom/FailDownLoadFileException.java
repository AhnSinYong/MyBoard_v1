package com.board.portfolio.exception.custom;

import lombok.Getter;

@Getter
public class FailDownLoadFileException extends CustomRuntimeException {
    public FailDownLoadFileException(){
        super("Fail file Download");
    }
    public FailDownLoadFileException(String msg){
        super(msg);
    }
}
