package com.labs339.platform.exception;

public class BaseBizException extends RuntimeException {

    private int code;

    public int getCode() {
        return this.code;
    }
    public void setCode(int code) {
        this.code = code;
    }

    public BaseBizException() {
        this.code = WalletBizError.SYSTEM_ERROR.getCode();
    }

    public BaseBizException(String message) {
        super(message);
        this.code = WalletBizError.SYSTEM_ERROR.getCode();
    }

    public BaseBizException(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public BaseBizException(String message, Throwable cause) {
        super(message, cause);
        this.code = WalletBizError.SYSTEM_ERROR.getCode();
    }

    public BaseBizException(Integer code,String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public BaseBizException(Throwable cause) {
        super(cause);
        this.code = WalletBizError.SYSTEM_ERROR.getCode();
    }

}
