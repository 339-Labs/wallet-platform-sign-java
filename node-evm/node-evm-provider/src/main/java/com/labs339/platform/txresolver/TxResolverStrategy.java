package com.labs339.platform.txresolver;

import com.labs339.platform.enums.ChainTxType;

public interface TxResolverStrategy {

    /**
     * 解析tx
     * @param tx
     * @param chainName 暂时不用，解析链不同交易
     * @return
     */
    String resolve(String tx,String chainName);

    ChainTxType getChainType();

}
