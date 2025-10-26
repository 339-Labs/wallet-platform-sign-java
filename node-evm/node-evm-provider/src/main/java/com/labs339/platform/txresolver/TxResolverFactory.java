package com.labs339.platform.txresolver;

import com.labs339.platform.enums.ChainTxType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class TxResolverFactory {

    // 注入 ApplicationContext
    @Autowired
    private ApplicationContext applicationContext;

    private static final Map<ChainTxType, TxResolverStrategy> resolverMap = new HashMap<>();

    // 使用 @PostConstruct 注解在 Bean 初始化时加载策略
    @PostConstruct
    public void init() {
        // 获取所有实现 AddressResolverStrategy 的 Bean
        Map<String, TxResolverStrategy> resolverBeans = applicationContext.getBeansOfType(TxResolverStrategy.class);

        // 遍历所有策略 Bean，将它们根据 ChainType 存入 resolverMap
        for (TxResolverStrategy strategy : resolverBeans.values()) {
            resolverMap.put(strategy.getChainType(), strategy);
        }
    }

    public static TxResolverStrategy getTxResolverStrategy(ChainTxType chainTxType) {
        return resolverMap.get(chainTxType);
    }

}
