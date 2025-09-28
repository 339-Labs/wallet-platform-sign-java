package com.labs339.platform.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.labs339.platform.baseDto.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("business_withdraw")
public class BusinessWithdrawModel extends BaseEntity {

    @TableId(value = "id",type = IdType.ASSIGN_ID)        // 映射 `id` 字段
    private Long id;

    @TableField(value = "business_id")  // 复合主键一部分
    private String businessId;

    @TableField(value = "user_id")      // 复合主键一部分
    private Long userId;

    /**
     *  WITHDRAW(1, "withdraw"),
     *  DEPOSIT(2, "deposit"),
     *  INTERNAL_TRANSFER(3, "internal_transfer");
     */
    @TableField(value = "op_type")
    private Integer opType;

    @TableField(value = "chain_name")
    private String chainName;

    @TableField(value = "chain_type")
    private String chainType;

    @TableField(value = "token_name")
    private String tokenName;

    @TableField(value = "from_address")
    private String fromAddress;

    @TableField(value = "to_address")
    private String toAddress;

    @TableField(value = "tag")
    private String tag;

    @TableField(value = "amount")
    private BigDecimal amount;

    @TableField(value = "fee")
    private BigDecimal fee;

    /**
     * 0 初始化 1 审核中 2 自动通过 3 审核通过 5 拒绝
     */
    @TableField(value = "audit_status")
    private Integer auditStatus;

    /**
     * 0 初始化 1 cancel 取消 2 审核处理 3 交易构建 build 4 交易失败 fail 6 交易成功 success
     */
    @TableField(value = "task_status")
    private Integer taskStatus;

    /**
     * 0 初始化
     */
    @TableField(value = "notify_status")
    private Integer notifyStatus;

    /**
     * 对账状态
     * 0 未对账 1 对账完成
     */
    @TableField(value = "check_status")
    private Integer checkStatus;

    /**
     * 审核时间
     */
    @TableField(value = "audit_finish_time")
    private LocalDateTime auditFinishTime;

    /**
     * 取消时间
     */
    @TableField(value = "cancel_time")
    private LocalDateTime cancelTime;

    @TableField(value = "risk_level")
    private Integer riskLevel;

    @TableField(value = "risk_tag")
    private String riskTag;

    /**
     * 区块号
     */
    @TableField(value = "block_number")
    private Integer blockNumber;

    /**
     * 交易hash
     */
    @TableField(value = "tx_hash")
    private String txHash;

    /**
     * 上链时间
     */
    @TableField(value = "on_chain_time")
    private LocalDateTime onChainTime;

    /**
     * 交易确认时间
     */
    @TableField(value = "tx_confirm_time")
    private LocalDateTime txConfirmTime;


}
