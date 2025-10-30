package com.labs339.platform.rpcclient;

import com.labs339.platform.config.ChainInfoConfig;
import com.labs339.platform.enums.ChainTxType;
import com.labs339.platform.txresolver.TxResolverStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@DependsOn("chainInfoConfig")
public class ChainClientManager implements DisposableBean {

    @Autowired
    private ChainInfoConfig chainInfoConfig;

    @Autowired
    private EvmClient evmClient;

    @Autowired
    private SolanaClient solanaClient;

    // 可以继续添加其他链类型的客户端
    // @Autowired
    // private CosmosClient cosmosClient;

    // 链名称 -> 客户端映射
    private final Map<String, ChainRpcClient<?>> clientRegistry = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Initializing BlockchainClientManager...");

        chainInfoConfig.getChainConfigMap().forEach((key, chainConfig) -> {
            // 可限制初始化 具体链
//            if (!chainConfig.getEnabled()) {
//                log.info("Chain [{}] is disabled, skipping", chainConfig.getChainName());
//                return;
//            }

            ChainTxType chainTxType = ChainTxType.getChainType(chainConfig.getChainType());
            ChainRpcClient<?> client = getClientByType(chainTxType);
            if (client != null) {
                client.initializeChain(chainConfig.getChainName(), chainConfig.getChainRpc());
                clientRegistry.put(chainConfig.getChainName(), client);
            } else {
                log.error("Unsupported chain type: {} for chain {}", chainConfig.getChainType(), chainConfig.getChainName());
            }
        });

        log.info("BlockchainClientManager initialized with {} chains", clientRegistry.size());
    }

    /**
     * 根据链类型获取对应的客户端
     */
    private ChainRpcClient<?> getClientByType(ChainTxType chainTxType) {
        switch (chainTxType) {
            case ChainTxType.Ethereum:
                return evmClient;
            case ChainTxType.Solana:
                return solanaClient;
            // case COSMOS:
            //     return cosmosClient;
            default:
                return null;
        }
    }

    /**
     * 获取指定链的客户端
     */
    public <T> T getClient(String chainName, Class<T> clientClass) {
        ChainRpcClient<?> client = clientRegistry.get(chainName);
        if (client == null) {
            throw new IllegalArgumentException("Chain not found: " + chainName);
        }
        return clientClass.cast(client.getClient(chainName));
    }


    /**
     * 检查指定链的健康状态
     */
    public boolean isChainHealthy(String chainName) {
        ChainRpcClient<?> client = clientRegistry.get(chainName);
        return client != null && client.isHealthy(chainName);
    }

    /**
     * 获取所有链的健康状态
     */
    public Map<String, Boolean> getAllHealthStatus() {
        Map<String, Boolean> statusMap = new HashMap<>();
        clientRegistry.forEach((chainName, client) -> {
            statusMap.put(chainName, client.isHealthy(chainName));
        });
        return statusMap;
    }


    /**
     * 重连指定链
     */
    public void reconnectChain(String chainName) {
        ChainRpcClient<?> client = clientRegistry.get(chainName);
        if (client != null) {
            client.reconnect(chainName);
        }
    }

    /**
     * 重连所有链
     */
    public void reconnectAll() {
        log.info("Reconnecting all chains");
        clientRegistry.forEach((chainName, client) -> client.reconnect(chainName));
    }

    /**
     * 定时健康检查（每5分钟）
     */
    @Scheduled(fixedRate = 300000)
    public void healthCheck() {
        log.debug("Starting scheduled health check for all chains");
        clientRegistry.forEach((chainName, client) -> {
            boolean healthy = client.isHealthy(chainName);
            if (!healthy) {
                log.warn("Chain [{}] is unhealthy, attempting reconnection", chainName);
                client.reconnect(chainName);
            }
        });
    }

    /**
     * 当 Spring 容器关闭时，所有实现了 DisposableBean 接口的 Bean 将自动调用该方法来进行资源释放、清理等操作。
     * @throws Exception
     */
    @Override
    public void destroy() throws Exception {
        log.info("Shutting down BlockchainClientManager");
        clientRegistry.forEach((chainName, client) -> {
            try {
                client.shutdown(chainName);
            } catch (Exception e) {
                log.error("Error shutting down chain [{}]", chainName, e);
            }
        });
        clientRegistry.clear();
        log.info("BlockchainClientManager shutdown completed");
    }
}
