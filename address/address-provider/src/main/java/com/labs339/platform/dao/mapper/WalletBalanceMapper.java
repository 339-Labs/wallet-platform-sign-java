package com.labs339.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.labs339.platform.dao.entity.WalletAddressModel;
import com.labs339.platform.dao.entity.WalletBalanceModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WalletBalanceMapper extends BaseMapper<WalletBalanceModel> {
}
