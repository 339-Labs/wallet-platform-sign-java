package com.labs339.platform.rsp;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class UserTokenRsp implements Serializable {

    private String tokenName;

    private BigDecimal totalAmount;

    private BigDecimal availableAmount;

    private BigDecimal lockAmount;

}
