package com.board.portfolio.exception.custom;

import lombok.Getter;

@Getter
public class NotFoundCookieException extends CustomRuntimeException {
    public NotFoundCookieException(){
        super("Not Found Cookie");
    }
    public NotFoundCookieException(String msg){
        super(msg);
    }
}
