package com.wsmrxd.bloglite.enums;

public enum ErrorCode {
    TAG_NOT_FOUND(52404),
    USER_NOT_FOUND(53404),
    INVALID_PASSWORD(52403);

    private final int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
