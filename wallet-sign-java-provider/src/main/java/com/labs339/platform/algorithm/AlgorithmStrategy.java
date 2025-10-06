package com.labs339.platform.algorithm;

import com.labs339.platform.dao.ExtendedKey;
import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;

public interface AlgorithmStrategy {

    AlgorithmType getAlgorithmType();

    KeyPair getKeyPair(String coin, int index) throws Exception;

    String sign(String coin,int index, String msg) throws Exception;

}
