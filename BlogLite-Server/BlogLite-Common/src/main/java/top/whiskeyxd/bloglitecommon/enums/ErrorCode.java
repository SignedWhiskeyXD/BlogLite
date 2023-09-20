package top.whiskeyxd.bloglitecommon.enums;

import lombok.Data;

public enum ErrorCode {

    /* BlogLite-Auth, 25XXX */
    USER_NOT_FOUND(25404, "User not found"),

    AUTHORIZE_FAILED(25400, "Invalid username password");

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode(){
        return this.code;
    }

    public String what(){
        return this.message;
    }

    private final int code;

    private final String message;
}
