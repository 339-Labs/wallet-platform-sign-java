package com.labs339.platform.addressresolver;

import com.labs339.platform.enums.ChainType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class AddressResolverFactory {

    // 注入 ApplicationContext
    @Autowired
    private ApplicationContext applicationContext;

    private static final Map<ChainType, AddressResolverStrategy> resolverMap = new HashMap<>();

    // 使用 @PostConstruct 注解在 Bean 初始化时加载策略
    @PostConstruct
    public void init() {
        // 获取所有实现 AddressResolverStrategy 的 Bean
        Map<String, AddressResolverStrategy> resolverBeans = applicationContext.getBeansOfType(AddressResolverStrategy.class);

        // 遍历所有策略 Bean，将它们根据 ChainType 存入 resolverMap
        for (AddressResolverStrategy strategy : resolverBeans.values()) {
            ChainType chainType = strategy.getChainType();
            resolverMap.put(chainType, strategy);
        }
    }

    public static AddressResolverStrategy getAddressResolverStrategy(ChainType chainType) {
        return resolverMap.get(chainType);
    }

}
