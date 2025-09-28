package com.labs339.platform.baseEnums;

public enum OpTypeEnums {

    WITHDRAW(1, "withdraw"),
    DEPOSIT(2, "deposit"),
    INTERNAL_TRANSFER(3, "internal_transfer");

    public final int code;
    public final String value;

    OpTypeEnums(int code, String value) {
        this.code = code;
        this.value = value;
    }

}
