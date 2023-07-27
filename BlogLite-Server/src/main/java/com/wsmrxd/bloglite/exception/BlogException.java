package com.wsmrxd.bloglite.exception;

import com.wsmrxd.bloglite.enums.ErrorCode;

public class BlogException extends RuntimeException{
    private final ErrorCode code;

    private final String message;

    public ErrorCode getCode(){
        return this.code;
    }

    public String what(){
        return message;
    }

    public BlogException(ErrorCode code, String message){
        this.code = code;
        this.message = message;
    }
}
