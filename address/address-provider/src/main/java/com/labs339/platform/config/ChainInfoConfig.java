package com.labs339.platform.config;

import com.labs339.platform.dao.entity.ChainConfigModel;
import com.labs339.platform.dao.entity.TokenConfigModel;
import com.labs339.platform.service.impl.ChainConfigServiceImpl;
import com.labs339.platform.service.impl.TokenConfigServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Slf4j
public class ChainInfoConfig {

    @Autowired
    private ChainConfigServiceImpl chainConfigService;

    @Autowired
    private TokenConfigServiceImpl tokenConfigService;

    private final Map<String, ChainConfigModel> chainConfigMap = new ConcurrentHashMap<>();
    private final Map<String, TokenConfigModel> tokenConfigMap = new ConcurrentHashMap<>();
    private final Map<String, List<TokenConfigModel>> tokenChainMap = new ConcurrentHashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        log.info("服务启动 -> 初始化配置缓存");
        refresh();
    }

    public void refresh() {
        log.info("开始刷新配置缓存...");

        // 刷新链配置
        List<ChainConfigModel> configModels = chainConfigService.list();
        chainConfigMap.clear();
        configModels.forEach(configModel ->
                chainConfigMap.put(configModel.getChainName(), configModel));

        // 刷新代币配置
        List<TokenConfigModel> configModelList = tokenConfigService.list();
        tokenConfigMap.clear();
        tokenChainMap.clear();
        configModelList.forEach(configModel -> {
            // k -value
            String key = String.join("-", configModel.getChainName(), configModel.getTokenName());
            tokenConfigMap.put(key, configModel);
            // k-list
            tokenChainMap.computeIfAbsent(configModel.getTokenName(), k -> new ArrayList<>())
                    .add(configModel);
        });

        log.info("配置缓存刷新完成: chains={}, tokens={},token chain={}", chainConfigMap.size(), tokenConfigMap.size(), tokenChainMap.size());
    }

}
