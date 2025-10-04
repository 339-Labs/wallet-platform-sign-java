package com.labs339.platform.algorithm;

import com.labs339.platform.dao.ExtendedKey;
import com.labs339.platform.enums.AlgorithmType;

public interface AlgorithmStrategy {

    AlgorithmType getAlgorithmType();

    ExtendedKey getKeyPair(int coin, int index) throws Exception;

    String sign(String coin,int index, String msg) throws Exception;

}
