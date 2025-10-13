package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseChainInfo;
import com.labs339.platform.baseDto.BaseEntity;
import lombok.Data;

@Data
@TableName("chain_config")
public class ChainConfigModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)        // 映射 `id` 字段
    private Long id;

    @TableField(value = "chain_id")                 // 映射 `chain_id` 字段
    private Integer chainId;

    @TableField(value = "chain_name")               // 映射 `chain_name` 字段
    private String chainName;

    @TableField(value = "chain_full_name")          // 映射 `chain_full_name` 字段
    private String chainFullName;

    @TableField(value = "chain_rpc")                // 映射 `chain_rpc` 字段
    private String chainRpc;

    @TableField(value = "chain_wss")                // 映射 `chain_wss` 字段
    private String chainWss;

    @TableField(value = "chain_type")               // 映射 `chain_type` 字段
    private String chainType;

    @TableField(value = "withdraw_confirm_block")   // 映射 `withdraw_comfirm_block` 字段
    private Integer withdrawConfirmBlock;

    @TableField(value = "deposit_confirm_block")    // 映射 `deposit_comfirm_block` 字段
    private Integer depositConfirmBlock;

    @TableField(value = "original_token")           // 映射 `original_token` 字段
    private String originalToken;

    @TableField(value = "original_token_decimal")   // 映射 `original_token_decimal` 字段
    private Integer originalTokenDecimal;

    public static BaseChainInfo toChainRsp(ChainConfigModel chainConfigModel){
        BaseChainInfo baseChainInfo = new BaseChainInfo();
        baseChainInfo.setChainId(chainConfigModel.getChainId());
        baseChainInfo.setChainName(chainConfigModel.getChainName());
        baseChainInfo.setChainFullName(chainConfigModel.getChainFullName());
        return baseChainInfo;
    }

}
