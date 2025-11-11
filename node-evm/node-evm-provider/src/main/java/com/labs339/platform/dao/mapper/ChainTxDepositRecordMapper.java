package com.labs339.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.labs339.platform.dao.entity.ChainBlockRecordModel;
import com.labs339.platform.dao.entity.ChainTxDepositRecordModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChainTxDepositRecordMapper extends BaseMapper<ChainTxDepositRecordModel> {
}
