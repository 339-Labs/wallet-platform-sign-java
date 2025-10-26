package com.labs339.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.labs339.platform.dao.entity.TokenConfigModel;
import com.labs339.platform.dao.mapper.TokenConfigMapper;
import com.labs339.platform.service.TokenConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenConfigServiceImpl extends ServiceImpl<TokenConfigMapper, TokenConfigModel> implements TokenConfigService {


}
