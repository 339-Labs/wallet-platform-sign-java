package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseEntity;
import lombok.Data;

@Data
@TableName("node_block_sync_height")
public class NodeBlockSyncModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)        // 映射 `id` 字段
    private Long id;

    @TableField(value = "latest_block_num")                 // 映射 `latest_block_num` 字段
    private Long latestBlockNum;

    @TableField(value = "current_block_num")               // 映射 `current_block_num` 字段
    private Long currentBlockNum;

    @TableField(value = "chain_config_id")          // 映射 `chain_config_id` 字段
    private Long chainConfigId;

    @TableField(value = "chain_id")                // 映射 `chain_id` 字段
    private Integer chainId;

    @TableField(value = "chain_name")                // 映射 `chain_name` 字段
    private String chainName;

}
