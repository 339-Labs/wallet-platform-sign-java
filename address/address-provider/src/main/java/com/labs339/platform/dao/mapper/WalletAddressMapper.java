package com.labs339.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.labs339.platform.dao.entity.AddressNonceModel;
import com.labs339.platform.dao.entity.WalletAddressModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WalletAddressMapper extends BaseMapper<WalletAddressModel> {
}
