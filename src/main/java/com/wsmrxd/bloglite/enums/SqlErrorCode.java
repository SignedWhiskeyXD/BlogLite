package com.wsmrxd.bloglite.enums;

public enum SqlErrorCode {
    NOT_FOUND(52404);

    private final int errorCode;

    SqlErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
