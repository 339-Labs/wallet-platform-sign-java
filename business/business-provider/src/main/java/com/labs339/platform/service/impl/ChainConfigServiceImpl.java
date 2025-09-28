package com.labs339.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.labs339.platform.dao.entity.ChainConfigModel;
import com.labs339.platform.dao.mapper.ChainConfigMapper;
import com.labs339.platform.service.ChainConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChainConfigServiceImpl extends ServiceImpl<ChainConfigMapper, ChainConfigModel> implements ChainConfigService {


}
