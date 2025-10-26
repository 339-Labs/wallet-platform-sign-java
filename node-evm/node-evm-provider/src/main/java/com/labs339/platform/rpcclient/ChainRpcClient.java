package com.labs339.platform.rpcclient;

import com.labs339.platform.enums.ChainTxType;

public interface ChainRpcClient<T> {

    /**
     * 初始化链连接
     * @param chainName 链名称
     * @param rpcUrl RPC地址
     */
    void initializeChain(String chainName, String rpcUrl);

    /**
     * 获取原生客户端实例（Web3j、SolanaClient等）
     * @param chainName 链名称
     * @return 客户端实例
     */
    T getClient(String chainName);

    /**
     * 检查连接健康状态
     * @param chainName 链名称
     * @return true-健康, false-不健康
     */
    boolean isHealthy(String chainName);

    /**
     * 重新连接指定链
     * @param chainName 链名称
     */
    void reconnect(String chainName);

    /**
     * 关闭指定链连接
     * @param chainName 链名称
     */
    void shutdown(String chainName);

    /**
     * 关闭所有连接
     */
    void shutdownAll();

    /**
     * 获取链交易类型
     * @return 链类型枚举
     */
    ChainTxType getChainType();

}
