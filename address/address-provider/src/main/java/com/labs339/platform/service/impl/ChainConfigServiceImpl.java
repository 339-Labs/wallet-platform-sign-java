package com.labs339.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.labs339.platform.baseDto.BaseChainInfo;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.config.ChainInfoConfig;
import com.labs339.platform.dao.entity.ChainConfigModel;
import com.labs339.platform.dao.mapper.ChainConfigMapper;
import com.labs339.platform.service.ChainConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@Slf4j
public class ChainConfigServiceImpl extends ServiceImpl<ChainConfigMapper, ChainConfigModel> implements ChainConfigService {

}
