package com.labs339.platform.req;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class TransferTokenReq implements Serializable {

    private Long userId;

    private String tokenAddress;

    private String tokenName;

    private BigDecimal amount;

    /**
     * 1 减少锁定，2 减少，3 新增锁定 ，4 新增
     */
    private Integer assetOpType;

}
