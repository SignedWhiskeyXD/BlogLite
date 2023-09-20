package top.whiskeyxd.bloglitecommon.exception;

import top.whiskeyxd.bloglitecommon.enums.ErrorCode;

public class BlogLiteException extends RuntimeException{

    private final ErrorCode errorCode;

    public BlogLiteException(ErrorCode errorCode){
        super(errorCode.what());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return this.errorCode;
    }
}
