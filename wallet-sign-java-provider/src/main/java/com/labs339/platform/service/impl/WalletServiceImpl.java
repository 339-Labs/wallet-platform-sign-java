package com.labs339.platform.service.impl;

import com.labs339.platform.algorithm.AlgorithmStrategy;
import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;
import com.labs339.platform.enums.CoinType;
import com.labs339.platform.exception.WalletBizError;
import com.labs339.platform.exception.WalletBizException;
import com.labs339.platform.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.labs339.platform.exception.WalletBizError.GIN_ERROR;
import static com.labs339.platform.exception.WalletBizError.KEN_GEN_ERROR;

@Slf4j
@Service
public class WalletServiceImpl implements WalletService {
    @Override
    public Boolean getSupportSignWay(String chain) {

        CoinType coinType = CoinType.getCoinType(chain);
        if (coinType == null) {
            return false;
        }
        return true;
    }

    @Override
    public List<KeyPair> generateKeyGen(String chain, Integer cursor,Integer size) {

        // index 从 cursor 开始
        CoinType coinType = CoinType.getCoinType(chain);
        AlgorithmStrategy algorithmStrategy = coinType.getAlgorithmStrategy();
        int end = cursor + size;
        List<KeyPair> keyPairList = new ArrayList<>();
        try {
            for (; cursor < end; cursor++) {
                KeyPair keyPair = algorithmStrategy.getKeyPair(coinType.getCoin(),cursor);
                keyPairList.add(keyPair);
            }
        }catch (Exception e){
            log.error("generateKeyGen error ,chain {}, cursor {}, size {}", chain, cursor, size,e);
            return keyPairList;
        }
        return keyPairList;
    }

    @Override
    public String sign(String chain, int index, String unSignMsg) {

        CoinType coinType = CoinType.getCoinType(chain);
        AlgorithmStrategy algorithmStrategy = coinType.getAlgorithmStrategy();
        try {
            String signMsg = algorithmStrategy.sign(coinType.getCoin(),index,unSignMsg);
            return signMsg;
        }catch (Exception e){
            log.error("sign error ,chain {}, cursor {}, unSignMsg {}", chain, index, unSignMsg, e);
            return "";
        }

    }
}
