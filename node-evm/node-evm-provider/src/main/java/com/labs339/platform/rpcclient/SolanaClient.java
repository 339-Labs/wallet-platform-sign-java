package com.labs339.platform.rpcclient;

import com.labs339.platform.basehttp.BaseHttpClient;
import com.labs339.platform.enums.ChainTxType;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SolanaClient extends BaseHttpClient implements ChainRpcClient{

    private final Map<String, String> rpcUrlMap = new ConcurrentHashMap<>();
    private final Map<String, Boolean> healthStatusMap = new ConcurrentHashMap<>();

    private static final int DEFAULT_MAX_RETRIES = 3;
    private static final long DEFAULT_RETRY_DELAY_MS = 2000;

    @Override
    public void initializeChain(String chainName, String rpcUrl) {
        log.info("初始化Solana链: {} -> {}", chainName, rpcUrl);

    }



    @Override
    public Object getClient(String chainName) {
        return null;
    }

    @Override
    public boolean isHealthy(String chainName) {
        return healthStatusMap.getOrDefault(chainName, false);
    }

    @Override
    public void reconnect(String chainName) {

    }

    @Override
    public void shutdown(String chainName) {

    }

    @Override
    public void shutdownAll() {

    }

    @Override
    public ChainTxType getChainType() {
        return ChainTxType.Solana;
    }
}
