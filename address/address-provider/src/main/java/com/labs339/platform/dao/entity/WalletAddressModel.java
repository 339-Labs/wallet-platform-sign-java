package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseEntity;
import lombok.Data;

@Data
@TableName("wallet_address")
public class WalletAddressModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)        // 映射 `id` 字段
    private Long id;

    @TableField(value = "user_id")         // 映射 `user_id` 字段
    private Long user_id;

    @TableField(value = "address")                // 映射 `address` 字段
    private String address;

    @TableField(value = "address_type")              // 映射 `address_type` 字段
    private String addressType;

    @TableField(value = "memo")           // 映射 `memo` 字段
    private String memo;

    @TableField(value = "chain_type")              // 映射 `chain_type` 字段
    private String chainType;

}
