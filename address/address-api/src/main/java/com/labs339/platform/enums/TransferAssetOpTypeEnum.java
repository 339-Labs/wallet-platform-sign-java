package com.labs339.platform.enums;

public enum TransferAssetOpTypeEnum {

    Lock(1, "锁定金额"),
    Sub(2, "扣减金额"),
    Add(3, "新增金额");

    private final Integer code;
    private final String description;

    TransferAssetOpTypeEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
