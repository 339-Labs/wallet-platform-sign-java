package com.labs339.platform.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WalletBizError {

    /**
     * common error
     */
    SYSTEM_ERROR(20000,"system error"),
    PARAM_ERROR(20001,"param error"),
    AUTH_ERROR(20002,"auth error"),
    BUSINESS_ERROR(20003,"business error"),

    NOT_SUPPORT_COIN_ERROR(40000,"not support this coin . error"),;

    private final int code;
    private final String message;

}
