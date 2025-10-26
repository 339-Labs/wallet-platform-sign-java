package com.labs339.platform.rpcclient;

import com.labs339.platform.enums.ChainTxType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class EvmClient implements ChainRpcClient<Web3j> {

    private final Map<String, Web3j> clientMap = new ConcurrentHashMap<>();

    private final Map<String, String> rpcUrlMap = new ConcurrentHashMap<>();

    private final Map<String, Boolean> healthStatusMap = new ConcurrentHashMap<>();


    private static final long DEFAULT_TIMEOUT_MS = 5000;
    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final long DEFAULT_RETRY_DELAY_MS = 2000;


    @Override
    public void initializeChain(String chainName, String rpcUrl) {

        rpcUrlMap.put(chainName, rpcUrl);

        for (int attempt = 1; attempt <= DEFAULT_MAX_RETRIES; attempt++) {
            try {
                log.info("EVM Initializing chain {}, attempt {},time {}", chainName, attempt, DEFAULT_MAX_RETRIES);

                HttpService httpService = new HttpService(rpcUrl);
                Web3j web3j = Web3j.build(httpService);

                // 测试连接
                Web3ClientVersion version = web3j.web3ClientVersion()
                        .sendAsync()
                        .get(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS);

                clientMap.put(chainName, web3j);
                healthStatusMap.put(chainName, true);

                log.info("EVM Chain {} connected. Client: {}", chainName, version.getWeb3ClientVersion());
                return;

            } catch (Exception e) {
                log.error("EVM Failed to initialize chain {}, attempt {},time {},{}", chainName, attempt, DEFAULT_MAX_RETRIES, e.getMessage(),e);

                if (attempt < DEFAULT_MAX_RETRIES) {
                    try {
                        Thread.sleep(DEFAULT_RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    log.error("EVM Failed to initialize chain ,end... ");
                    healthStatusMap.put(chainName, false);
                }
            }
        }

    }

    @Override
    public Web3j getClient(String chainName) {

        Web3j web3j = clientMap.get(chainName);

        if (web3j == null) {
            log.error("EVM Client not support {}", chainName);
            return null;
        }

        if (!isHealthy(chainName)) {
            log.warn("EVM Chain {} unhealthy, attempting reconnection", chainName);
            reconnect(chainName);
        }

        return clientMap.get(chainName);
    }

    @Override
    public boolean isHealthy(String chainName) {
        Web3j web3j = clientMap.get(chainName);
        if (web3j == null) {
            return false;
        }

        try {
            web3j.web3ClientVersion()
                    .sendAsync()
                    .get(DEFAULT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            return true;
        } catch (Exception e) {
            log.error("EVM Health check failed for {}: {}", chainName, e.getMessage());
            return false;
        }
    }

    @Override
    public void reconnect(String chainName) {
        Web3j oldClient = clientMap.get(chainName);
        if (oldClient != null) {
            try {
                oldClient.shutdown();
            } catch (Exception e) {
                log.error("EVM Error shutting down old connection {}", chainName, e);
            }
        }

        String rpcUrl = rpcUrlMap.get(chainName);
        if (rpcUrl != null) {
            healthStatusMap.put(chainName, false);
            initializeChain(chainName, rpcUrl);
        }
    }

    @Override
    public void shutdown(String chainName) {
        Web3j web3j = clientMap.remove(chainName);
        if (web3j != null) {
            try {
                web3j.shutdown();
                log.info("EVM Chain {} shutdown completed", chainName);
            } catch (Exception e) {
                log.error("EVM Error shutting down {}", chainName, e);
            }
        }
        healthStatusMap.remove(chainName);
        rpcUrlMap.remove(chainName);
    }

    @Override
    public void shutdownAll() {
        log.info("EVM Shutting down all connections");
        clientMap.keySet().forEach(this::shutdown);
    }

    @Override
    public ChainTxType getChainType() {
        return ChainTxType.Ethereum;
    }

//    @Autowired
//    private ChainInfoConfig chainInfoConfig;

//    // 多链Web3j实例映射
//    private final Map<String, Web3j> web3jMap = new ConcurrentHashMap<>();

//    /**
//     * 初始化方法 - 启动时自动执行
//     * 可以从配置文件或数据库加载链配置
//     */
//    @PostConstruct
//    public void init() {
//        log.info("Initializing EvmRpcClient...");
//
//        // 初始化所有链的Web3j实例
//        chainInfoConfig.getChainConfigMap().forEach((s, chainConfig) -> {
//            try {
//                Web3j web3j = Web3j.build(new HttpService(chainConfig.getChainRpc()));
//                web3jMap.put(chainConfig.getChainName(), web3j);
//                // 测试连接
//                String version = web3j.web3ClientVersion().send().getWeb3ClientVersion();
//                log.info("Chain [{}] connected successfully. Client version: {}", chainConfig.getChainName(), version);
//            } catch (Exception e) {
//                log.error("Failed to initialize chain [{}] with RPC: {}", chainConfig.getChainName(), chainConfig.getChainRpc(), e);
//            }
//        });
//
//        log.info("EvmRpcClient initialized successfully with {} chains", web3jMap.size());
//    }

}
