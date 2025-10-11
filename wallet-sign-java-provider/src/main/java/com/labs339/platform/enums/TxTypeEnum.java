package com.labs339.platform.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 目前仅仅支持EVM 的 chainid , 签名 v 使用
 * https://chainlist.org/
 */
@Getter
@AllArgsConstructor
public enum TxTypeEnum {

    Ether(1,"EIP-1559",CoinType.Ether),
    Ether_Sepolia(11155111,"EIP-1559",CoinType.Ether_Sepolia),
    Arbitrum(42161,"EIP-1559",CoinType.Arbitrum),
    Arbitrum_Sepolia(421614,"EIP-1559",CoinType.Arbitrum_Sepolia),
    Optimistic(10,"EIP-1559",CoinType.Optimistic),
    Optimistic_Goerli(420,"EIP-1559",CoinType.Optimistic_Sepolia),
    Tron(728126428,"Legacy",CoinType.Tron),// 无 chainId
    ;

    private final int chainId;
    private final String txType;
    private final CoinType coinType;

    public static TxTypeEnum getByType(CoinType coinType) {
        for (TxTypeEnum txTypeEnum : TxTypeEnum.values()) {
            if (txTypeEnum.getCoinType().equals(coinType)) {
                return txTypeEnum;
            }
        }
        return null;
    }

    public static boolean isEvmEip1559(TxTypeEnum txTypeEnum) {
        return txTypeEnum.txType.equals("EIP-1559");
    }

    public static boolean isEvmEip155(TxTypeEnum txTypeEnum) {
        return txTypeEnum.txType.equals("EIP-155");
    }

    public static boolean isLegacy(TxTypeEnum txTypeEnum) {
        return txTypeEnum.txType.equals("Legacy");
    }

}
