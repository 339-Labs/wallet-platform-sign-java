package com.labs339.platform.enums;

import lombok.Getter;

@Getter
public enum ChainType {

    // secp256k1
    Ether("60","ETH","Ether","EVM"),
    Arbitrum("9001","ARB1","Arbitrum","EVM"),
    Optimistic("614","OPT","Optimistic","EVM"),

    Tron("195","TRX","Tron","Tron"),

    Bitcoin("0","BTC","Tron","Tron"),

    // edd25519
    Solana("501","SOL","Solana","Solana"),
    Cardano("1815","ADA","Cardano","Cardano"),
    NEAR("397","NEAR","NEAR Protocol","NEAR Protocol"),;

    private final String coinType;
    private final String symbol;
    private final String chainName;
    private final String chainType;


    ChainType(String coinType, String symbol, String chainName, String chainType) {
        this.coinType = coinType;
        this.symbol = symbol;
        this.chainName = chainName;
        this.chainType = chainType;
    }

    public static ChainType getSignAddressType(ChainType chainType) {
        if (chainType == null) {
            return null;
        }
        if (chainType.getChainType() == ChainType.Ether.getChainType()) {
            return Ether;
        }else {
            return chainType;
        }
    }

}
