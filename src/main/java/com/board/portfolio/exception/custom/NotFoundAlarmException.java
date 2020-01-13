package com.board.portfolio.exception.custom;

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
