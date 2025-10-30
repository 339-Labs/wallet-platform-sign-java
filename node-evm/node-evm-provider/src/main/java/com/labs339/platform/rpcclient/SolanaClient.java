package com.labs339.platform.rpcclient;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.labs339.platform.basehttp.BaseHttpClient;
import com.labs339.platform.basehttp.HttpResponse;
import com.labs339.platform.enums.ChainTxType;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class SolanaClient implements ChainRpcClient<SolanaClient.SolanaRpc>{

    private final Map<String, SolanaRpc> clientMap = new ConcurrentHashMap<>();
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
                log.info("Solana Initializing chain {}, attempt {}/{}", chainName, attempt, DEFAULT_MAX_RETRIES);

                // 创建Solana RPC包装器
                SolanaRpc solanaRpc = new SolanaRpc(rpcUrl);

                // 测试连接 - 获取版本信息
                RpcResponse versionResponse = solanaRpc.getVersion();

                if (versionResponse.isSuccess()) {
                    clientMap.put(chainName, solanaRpc);
                    healthStatusMap.put(chainName, true);

                    String version = versionResponse.getResult().get("solana-core").getAsString();
                    log.info("Solana Chain {} connected. Version: {}", chainName, version);
                    return;
                } else {
                    throw new RuntimeException("RPC Error: " + versionResponse.getError());
                }

            } catch (Exception e) {
                log.error("Solana Failed to initialize chain {}, attempt {}/{}: {}",
                        chainName, attempt, DEFAULT_MAX_RETRIES, e.getMessage(), e);

                if (attempt < DEFAULT_MAX_RETRIES) {
                    try {
                        Thread.sleep(DEFAULT_RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    log.error("Solana Failed to initialize chain {}, end...", chainName);
                    healthStatusMap.put(chainName, false);
                }
            }
        }
    }

    @Override
    public SolanaRpc getClient(String chainName) {
        SolanaRpc wrapper = clientMap.get(chainName);

        if (wrapper == null) {
            log.error("Solana Client not support {}", chainName);
            return null;
        }

        if (!isHealthy(chainName)) {
            log.warn("Solana Chain {} unhealthy, attempting reconnection", chainName);
            reconnect(chainName);
        }

        return clientMap.get(chainName);
    }

    @Override
    public boolean isHealthy(String chainName) {
        SolanaRpc wrapper = clientMap.get(chainName);
        if (wrapper == null) {
            return false;
        }

        try {
            RpcResponse response = wrapper.getBlockHeight();
            boolean healthy = response != null && response.isSuccess();

            if (healthy) {
                log.debug("Solana Health check passed for {}", chainName);
            } else {
                log.warn("Solana Health check failed for {}: {}",
                        chainName, response != null ? response.getError() : "null response");
            }

            return healthy;
        } catch (Exception e) {
            log.error("Solana Health check failed for {}: {}", chainName, e.getMessage());
            return false;
        }
    }

    @Override
    public void reconnect(String chainName) {
        SolanaRpc oldClient = clientMap.get(chainName);
        if (oldClient != null) {
            try {
                // BaseHttpClient 基于 OkHttp，连接池自动管理，无需手动关闭
                log.debug("Solana Preparing to reconnect {}", chainName);
            } catch (Exception e) {
                log.error("Solana Error during reconnection preparation {}", chainName, e);
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
        SolanaRpc wrapper = clientMap.remove(chainName);
        if (wrapper != null) {
            try {
                // BaseHttpClient 基于 OkHttp，连接池会自动清理
                log.info("Solana Chain {} shutdown completed", chainName);
            } catch (Exception e) {
                log.error("Solana Error shutting down {}", chainName, e);
            }
        }
        healthStatusMap.remove(chainName);
        rpcUrlMap.remove(chainName);
    }

    @Override
    @PreDestroy
    public void shutdownAll() {
        log.info("Solana Shutting down all connections");
        clientMap.keySet().forEach(this::shutdown);
    }

    @Override
    public ChainTxType getChainType() {
        return ChainTxType.Solana;
    }


    /**
     * Solana RPC包装器
     * 封装BaseHttpClient，提供Solana专用RPC方法
     */
    public static class SolanaRpc {
        private final BaseHttpClient httpClient;
        private final String rpcUrl;
        private final Gson gson;
        private final AtomicLong requestId;

        public SolanaRpc(String rpcUrl) {
            this.rpcUrl = rpcUrl;
            this.httpClient = new BaseHttpClient();
            this.gson = new Gson();
            this.requestId = new AtomicLong(1);

            // 配置超时
            this.httpClient.configureTimeouts(10000, 30000, 30000);

            // 配置重试拦截器
            this.httpClient.addInterceptor(new RetryInterceptor(3, 2000));
        }

        /**
         * 发送RPC请求
         */
        public RpcResponse sendRpcRequest(String method, Object... params) throws IOException {
            Map<String, Object> request = new HashMap<>();
            request.put("jsonrpc", "2.0");
            request.put("id", requestId.getAndIncrement());
            request.put("method", method);
            request.put("params", params);

            String requestBody = gson.toJson(request);
            HttpResponse response = httpClient.post(rpcUrl, requestBody);

            if (!response.isSuccess()) {
                throw new IOException("HTTP Error " + response.getStatusCode() + ": " + response.getBody());
            }

            return parseRpcResponse(response.getBody());
        }

        /**
         * 解析RPC响应
         */
        private RpcResponse parseRpcResponse(String responseBody) {
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

            if (jsonResponse.has("error")) {
                JsonObject error = jsonResponse.getAsJsonObject("error");
                return new RpcResponse(null, error, false);
            }

            JsonObject result = jsonResponse.has("result") ? jsonResponse.get("result").getAsJsonObject() : null;
            return new RpcResponse(result, null, true);
        }

        // ============ Solana RPC方法 ============

        /**
         * 获取账户余额
         */
        public RpcResponse getBalance(String publicKey) throws IOException {
            return sendRpcRequest("getBalance", publicKey);
        }

        /**
         * 获取账户信息
         */
        public RpcResponse getAccountInfo(String publicKey) throws IOException {
            Map<String, String> config = new HashMap<>();
            config.put("encoding", "jsonParsed");
            return sendRpcRequest("getAccountInfo", publicKey, config);
        }

        /**
         * 获取区块高度
         */
        public RpcResponse getBlockHeight() throws IOException {
            return sendRpcRequest("getBlockHeight");
        }

        /**
         * 获取最新区块哈希
         */
        public RpcResponse getLatestBlockhash() throws IOException {
            return sendRpcRequest("getLatestBlockhash");
        }

        /**
         * 发送交易
         */
        public RpcResponse sendTransaction(String signedTransaction) throws IOException {
            Map<String, String> config = new HashMap<>();
            config.put("encoding", "base64");
            return sendRpcRequest("sendTransaction", signedTransaction, config);
        }

        /**
         * 获取交易状态
         */
        public RpcResponse getTransaction(String signature) throws IOException {
            Map<String, String> config = new HashMap<>();
            config.put("encoding", "jsonParsed");
            return sendRpcRequest("getTransaction", signature, config);
        }

        /**
         * 获取Slot
         */
        public RpcResponse getSlot() throws IOException {
            return sendRpcRequest("getSlot");
        }

        /**
         * 获取版本信息
         */
        public RpcResponse getVersion() throws IOException {
            return sendRpcRequest("getVersion");
        }

        /**
         * 获取链上程序账户列表
         */
        public RpcResponse getProgramAccounts(String programId) throws IOException {
            Map<String, String> config = new HashMap<>();
            config.put("encoding", "jsonParsed");
            return sendRpcRequest("getProgramAccounts", programId, config);
        }

        /**
         * 模拟交易
         */
        public RpcResponse simulateTransaction(String transaction) throws IOException {
            Map<String, String> config = new HashMap<>();
            config.put("encoding", "base64");
            return sendRpcRequest("simulateTransaction", transaction, config);
        }

        /**
         * 获取Token账户余额
         */
        public RpcResponse getTokenAccountBalance(String tokenAccountAddress) throws IOException {
            Map<String, String> config = new HashMap<>();
            config.put("encoding", "jsonParsed");
            return sendRpcRequest("getTokenAccountBalance", tokenAccountAddress, config);
        }

        /**
         * 获取地址拥有的所有Token账户
         */
        public RpcResponse getTokenAccountsByOwner(String ownerAddress, String tokenProgramId) throws IOException {
            Map<String, Object> filter = new HashMap<>();
            filter.put("programId", tokenProgramId);

            Map<String, String> config = new HashMap<>();
            config.put("encoding", "jsonParsed");

            return sendRpcRequest("getTokenAccountsByOwner", ownerAddress, filter, config);
        }

        /**
         * 获取RPC URL
         */
        public String getRpcUrl() {
            return rpcUrl;
        }

        /**
         * 获取底层HTTP客户端
         */
        public BaseHttpClient getHttpClient() {
            return httpClient;
        }
    }

    /**
     * RPC响应封装类
     */
    public static class RpcResponse {
        private final JsonObject result;
        private final JsonObject error;
        private final boolean success;

        public RpcResponse(JsonObject result, JsonObject error, boolean success) {
            this.result = result;
            this.error = error;
            this.success = success;
        }

        public JsonObject getResult() {
            return result;
        }

        public JsonObject getError() {
            return error;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getResultAsString() {
            return result != null ? result.toString() : null;
        }

        @Override
        public String toString() {
            if (success) {
                return "RpcResponse{success=true, result=" + result + "}";
            } else {
                return "RpcResponse{success=false, error=" + error + "}";
            }
        }
    }

    /**
     * 重试拦截器
     */
    private static class RetryInterceptor implements Interceptor {
        private final int maxRetries;
        private final int retryInterval;

        public RetryInterceptor(int maxRetries, int retryInterval) {
            this.maxRetries = maxRetries;
            this.retryInterval = retryInterval;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response = null;
            IOException exception = null;

            for (int attempt = 0; attempt <= maxRetries; attempt++) {
                try {
                    response = chain.proceed(request);

                    // 如果响应成功或不可重试的错误码，直接返回
                    if (response.isSuccessful() || !shouldRetry(response.code())) {
                        return response;
                    }

                    // 关闭响应体
                    response.close();

                } catch (IOException e) {
                    exception = e;

                    if (attempt == maxRetries) {
                        throw e;
                    }
                }

                // 等待后重试
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(retryInterval * (attempt + 1));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", e);
                    }
                }
            }

            if (exception != null) {
                throw exception;
            }

            return response;
        }

        private boolean shouldRetry(int code) {
            // 429: 速率限制, 502/503/504: 服务器错误
            return code == 429 || code == 502 || code == 503 || code == 504;
        }
    }

}
