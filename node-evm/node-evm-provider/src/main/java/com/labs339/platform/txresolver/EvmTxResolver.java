package com.labs339.platform.txresolver;

import com.labs339.platform.dao.entity.ChainConfigModel;
import com.labs339.platform.enums.ChainTxType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EvmTxResolver implements TxResolverStrategy {

    @Override
    public String resolve(String tx, ChainConfigModel chainConfig) {



        return null;
    }

    @Override
    public ChainTxType getChainType() {
        return ChainTxType.Ethereum;
    }


}
