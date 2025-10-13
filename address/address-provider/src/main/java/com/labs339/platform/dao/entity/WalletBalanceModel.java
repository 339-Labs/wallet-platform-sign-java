package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("wallet_balance")
public class WalletBalanceModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)        // 映射 `id` 字段
    private Long id;

    @TableField(value = "user_id")         // 映射 `user_id` 字段
    private Long userId;

    @TableField(value = "token_name")                // 映射 `token_name` 字段
    private String tokenName;

    @TableField(value = "total_amount")              // 映射 `total_amount` 字段
    private BigDecimal totalAmount;

    @TableField(value = "available_amount")           // 映射 `available_amount` 字段
    private BigDecimal availableAmount;

    @TableField(value = "lock_amount")              // 映射 `lock_amount` 字段
    private BigDecimal lockAmount;

}
