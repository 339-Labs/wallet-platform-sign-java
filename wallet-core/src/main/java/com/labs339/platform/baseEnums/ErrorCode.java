package com.labs339.platform.baseEnums;

public enum ErrorCode {
    SUCCESS(0, "success"),
    INVALID_PARAM(1001, "参数不合法"),
    INTERNAL_ERROR(500, "系统错误");

    public final int code;
    public final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
