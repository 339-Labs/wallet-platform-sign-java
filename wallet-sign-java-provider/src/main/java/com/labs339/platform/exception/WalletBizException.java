package com.labs339.platform.exception;

public class WalletBizException extends BaseBizException {

    public WalletBizException(String message) {
        super(WalletBizError.BUSINESS_ERROR.getCode(),message);
    }

    public WalletBizException(String message, Throwable cause) {
        super(WalletBizError.BUSINESS_ERROR.getCode(),message, cause);
    }

}
