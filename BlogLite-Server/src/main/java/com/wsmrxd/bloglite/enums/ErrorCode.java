package com.wsmrxd.bloglite.enums;

public enum ErrorCode {

    BAD_REQUEST(52400),

    TAG_NOT_FOUND(52404),

    LOGIN_ERROR(53400),

    USER_NOT_FOUND(53404),

    BLOG_NOT_FOUND(54404),

    INVALID_PASSWORD(52403);

    private final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
