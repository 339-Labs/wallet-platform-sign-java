package com.labs339.platform.enums;

import lombok.Getter;

@Getter
public enum ChainTxType {

    // secp256k1 所有evm同源链都是用这个来调用客户端方式,测试和正式环境 type一样，chainName不一致
    Ethereum("ETH","Ethereum","Erc20"),

    Tron("TRX","Tron","Tron"),

    Bitcoin("BTC","BTC","Bitcoin"),

    // edd25519
    Solana("SOL","Solana","Solana"),
    Cardano("ADA","Cardano","Cardano"),
    Near("NEAR","Near_Protocol","Near_Protocol"),;

    private final String symbol;
    private final String chainName;
    private final String chainType;


    ChainTxType(String symbol, String chainName, String chainType) {
        this.symbol = symbol;
        this.chainName = chainName;
        this.chainType = chainType;
    }

    // 通过初始化类配置的chainType来获取链的交易类型
    public static ChainTxType getChainType(String chainType) {
        for (ChainTxType chainTxTypeEnum : ChainTxType.values()) {
            if (chainTxTypeEnum.getChainType().equals(chainType)) {
                return chainTxTypeEnum;
            }
        }
        return null;
    }

}
