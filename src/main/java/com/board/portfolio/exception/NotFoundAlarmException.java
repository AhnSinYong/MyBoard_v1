package com.board.portfolio.exception;

import lombok.Getter;

@Getter
public class NotFoundAlarmException extends CustomRuntimeException {
    public NotFoundAlarmException(){
        super("Not found alarm");
    }
    public NotFoundAlarmException(String msg){
        super(msg);
    }
}
