package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseEntity;
import com.labs339.platform.baseDto.BaseTokenInfo;
import lombok.Data;

@Data
@TableName("token_config")
public class TokenConfigModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)        // 映射 `id` 字段
    private Long id;

    @TableField(value = "chain_config_id")         // 映射 `chain_config_id` 字段
    private Long chainConfigId;

    @TableField(value = "chain_id")                // 映射 `chain_id` 字段
    private Integer chainId;

    @TableField(value = "chain_name")              // 映射 `chain_name` 字段
    private String chainName;

    @TableField(value = "token_address")           // 映射 `token_address` 字段
    private String tokenAddress;

    @TableField(value = "token_name")              // 映射 `token_name` 字段
    private String tokenName;

    @TableField(value = "token_full_name")         // 映射 `token_full_name` 字段
    private String tokenFullName;

    @TableField(value = "token_decimal")           // 映射 `token_decimal` 字段
    private Integer tokenDecimal;

    public static BaseTokenInfo toTokenRsp(TokenConfigModel tokenConfigModel){
        BaseTokenInfo baseTokenInfo = new BaseTokenInfo();
        baseTokenInfo.setChainId(tokenConfigModel.getChainId());
        baseTokenInfo.setChainName(tokenConfigModel.getChainName());
        baseTokenInfo.setChainFullName(tokenConfigModel.getTokenFullName());
        baseTokenInfo.setTokenName(tokenConfigModel.getTokenName());
        baseTokenInfo.setTokenFullName(tokenConfigModel.getTokenFullName());
        baseTokenInfo.setTokenDecimal(tokenConfigModel.getTokenDecimal());
        return baseTokenInfo;
    }

}
