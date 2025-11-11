package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseEntity;
import lombok.Data;

import java.time.LocalTime;

@Data
@TableName("chain_tx_deposit_record")
public class ChainTxDepositRecordModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)        // 映射 `id` 字段
    private Long id;

    @TableField(value = "chain_id")                // 映射 `chain_id` 字段
    private Integer chainId;

    @TableField(value = "chain_config_id")          // 映射 `chain_config_id` 字段
    private Long chainConfigId;

    @TableField(value = "chain_name")                // 映射 `chain_name` 字段
    private String chainName;

    @TableField(value = "block_height")                 // 映射 `latest_block_num` 字段
    private Long blockHeight;

    @TableField(value = "block_hash")               // 映射 `current_block_num` 字段
    private String blockHash;

    @TableField(value = "tx_hash")               // 映射 `current_block_num` 字段
    private String txHash;

    @TableField(value = "output_index")               // 映射 `current_block_num` 字段
    private Integer outputIndex;

    @TableField(value = "tx_from")
    private String txFrom;

    @TableField(value = "tx_to")
    private String txTo;

    @TableField(value = "tx_token_address")
    private String txTokenAddress;

    @TableField(value = "tag")
    private String tag;

    @TableField(value = "tx_amount")
    private String txAmount;

    @TableField(value = "tx_fee")
    private String txFee;

    @TableField(value = "tx_time")
    private LocalTime txTime;

    @TableField(value = "nonce")
    private Long nonce;

    @TableField(value = "notify_status")
    private String notifyStatus;

    @TableField(value = "risk_level")
    private String riskLevel;

}
