package com.wsmrxd.bloglite.exception;

import com.wsmrxd.bloglite.enums.SqlErrorCode;

public class BlogSqlException extends RuntimeException{
    private final SqlErrorCode code;

    private final String message;

    public SqlErrorCode getCode(){
        return this.code;
    }

    public String what(){
        return message;
    }

    public BlogSqlException(SqlErrorCode code, String message){
        this.code = code;
        this.message = message;
    }
}
