package com.labs339.platform.config;

import com.labs339.platform.dao.entity.ChainConfigModel;
import com.labs339.platform.dao.entity.TokenConfigModel;
import com.labs339.platform.service.impl.ChainConfigServiceImpl;
import com.labs339.platform.service.impl.TokenConfigServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Getter
@Slf4j
public class ChainInfoConfig {

    @Autowired
    private ChainConfigServiceImpl chainConfigService;

    @Autowired
    private TokenConfigServiceImpl tokenConfigService;

    private Map<String, ChainConfigModel> chainConfigMap = new ConcurrentHashMap<>();

    private Map<String, TokenConfigModel> tokenConfigMap = new ConcurrentHashMap<>();


    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        List<ChainConfigModel> configModels = chainConfigService.list();
        configModels.forEach(configModel -> {
            chainConfigMap.put(configModel.getChainName(), configModel);
        });

        List<TokenConfigModel> configModelList = tokenConfigService.list();
        configModelList.forEach(configModel -> {
            String key = String.join("-", configModel.getChainName(), configModel.getTokenName());
            tokenConfigMap.put(key, configModel);
        });
    }

}
