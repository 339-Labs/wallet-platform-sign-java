package com.labs339.platform.enums;

import com.labs339.platform.algorithm.*;
import com.labs339.platform.exception.WalletBizError;
import com.labs339.platform.exception.WalletBizException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * https://github.com/satoshilabs/slips/blob/master/slip-0044.md
 */
@Getter
public enum CoinType {

    // secp256k1
    Bitcoin("0","0x80000000","BTC","Bitcoin",DepthEnum.Five,false, Ecdsa_secp256k1_btc.class),
    Ethereum("60","0x8000003c","ETH","Ethereum",DepthEnum.Five,true, Ecdsa_secp256k1_evm.class),
    Ethereum_Sepolia("60","0x8000003c","ETH","Ethereum_Sepolia",DepthEnum.Five,true, Ecdsa_secp256k1_evm.class),
    Arbitrum("9001","0x80002329","ARB1","Arbitrum",DepthEnum.Five,true, Ecdsa_secp256k1_evm.class),
    Arbitrum_Sepolia("9001","0x80002329","ARB1","Arbitrum_Sepolia",DepthEnum.Five,true, Ecdsa_secp256k1_evm.class),
    Optimistic("614","0x80000266","OPT","Optimistic",DepthEnum.Five,true, Ecdsa_secp256k1_evm.class),
    Optimistic_Sepolia("614","0x80000266","OPT","Optimistic_Sepolia",DepthEnum.Five,true, Ecdsa_secp256k1_evm.class),
    Tron("195","0x800000c3","TRX","Tron",DepthEnum.Five,true, Ecdsa_secp256k1_evm.class),

    // edd25519
    Solana("501","0x800001f5","SOL","Solana",DepthEnum.Four,false, Eddsa_ed25519.class),
    Solana_Test("501","0x800001f5","SOL","Solana_Test",DepthEnum.Four,false, Eddsa_ed25519.class),
    Cardano("1815","0x80000717","ADA","Cardano",DepthEnum.Five,false, Eddsa_ed25519.class),
    NEAR("397","0x8000018d","NEAR","Near_Protocol",DepthEnum.Three,false, Eddsa_ed25519.class),;

    private final String coinType;
    private final String pathComponent;
    private final String symbol;
    private final String coin;
    private final DepthEnum depth;
    private final Boolean isEvm;
    private final Class<? extends AlgorithmStrategy> algorithmStrategyClass;

    @Autowired
    private ApplicationContext applicationContext; // 获取 Spring 上下文

    CoinType(String coinType, String pathComponent, String symbol, String coin, DepthEnum depth, Boolean isEvm, Class<? extends AlgorithmStrategy> algorithmStrategyClass) {
        this.coinType = coinType;
        this.pathComponent = pathComponent;
        this.symbol = symbol;
        this.coin = coin;
        this.depth = depth;
        this.isEvm = isEvm;
        this.algorithmStrategyClass = algorithmStrategyClass;
    }


    public static CoinType getCoinType(String chainName) {
        for (CoinType coinType : CoinType.values()) {
            if (coinType.getCoin().equals(chainName)) {
                return coinType;
            }
        }
        throw new WalletBizException(WalletBizError.NOT_SUPPORT_COIN_ERROR);
    }

    public AlgorithmStrategy getAlgorithmStrategy() {
        // 从 Spring 容器中获取对应的策略 Bean
        return applicationContext.getBean(algorithmStrategyClass);
    }


}
