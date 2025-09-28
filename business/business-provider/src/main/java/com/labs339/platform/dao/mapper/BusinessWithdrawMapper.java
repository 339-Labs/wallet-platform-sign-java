package com.labs339.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.labs339.platform.dao.entity.BusinessWithdrawModel;
import com.labs339.platform.dao.entity.ChainConfigModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface BusinessWithdrawMapper extends BaseMapper<BusinessWithdrawModel> {
}
