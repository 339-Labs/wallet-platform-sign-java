package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseEntity;
import lombok.Data;

@Data
@TableName("address_nonce")
public class AddressNonceModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField(value = "address")
    private String address;

    @TableField(value = "nonce")
    private Integer nonce;

    @TableField(value = "chain_id")
    private Integer chainId;

    @TableField(value = "chain_name")
    private String chainName;


}
