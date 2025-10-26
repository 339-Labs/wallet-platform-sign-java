package com.labs339.platform.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.labs339.platform.dao.entity.ChainConfigModel;
import com.labs339.platform.dao.entity.TokenConfigModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenConfigMapper extends BaseMapper<TokenConfigModel> {
}
