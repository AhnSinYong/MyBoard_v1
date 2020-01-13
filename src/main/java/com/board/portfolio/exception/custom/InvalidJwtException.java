package com.board.portfolio.exception.custom;

import lombok.Getter;

@Getter
public class InvalidJwtException extends CustomRuntimeException {
    public InvalidJwtException(){
        super("Invalid JwtToken");
    }
    public InvalidJwtException(String msg){
        super(msg);
    }
}
