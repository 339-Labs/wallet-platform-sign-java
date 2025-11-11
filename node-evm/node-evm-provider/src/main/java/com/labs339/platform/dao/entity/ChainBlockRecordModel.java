package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseEntity;
import lombok.Data;

@Data
@TableName("chain_block_record")
public class ChainBlockRecordModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)        // 映射 `id` 字段
    private Long id;

    @Deprecated
    @TableField(value = "block_height")                 // 映射 `latest_block_num` 字段
    private Long blockHeight;

    @TableField(value = "block_hash")               // 映射 `current_block_num` 字段
    private String blockHash;

    @TableField(value = "chain_config_id")          // 映射 `chain_config_id` 字段
    private Long chainConfigId;

    @TableField(value = "chain_id")                // 映射 `chain_id` 字段
    private Integer chainId;

    @TableField(value = "chain_name")                // 映射 `chain_name` 字段
    private String chainName;

    @TableField(value = "handle_contract")       //是否处理合约，默认否
    private Boolean handleContract;

}
