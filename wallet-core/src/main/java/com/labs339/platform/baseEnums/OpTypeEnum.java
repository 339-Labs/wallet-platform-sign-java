package com.labs339.platform.baseEnums;

public enum OpTypeEnum {

    WITHDRAW(1, "withdraw"),
    DEPOSIT(2, "deposit"),
    INTERNAL_TRANSFER(3, "internal_transfer");

    public final int code;
    public final String value;

    OpTypeEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }

}
