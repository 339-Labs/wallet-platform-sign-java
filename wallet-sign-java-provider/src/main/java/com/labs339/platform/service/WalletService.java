package com.labs339.platform.service;

import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;

import java.util.List;

public interface WalletService {

    Boolean getSupportSignWay(String chain);

    List<KeyPair> generateKeyGen(String chain, Integer cursor, Integer size);

    String sign(String chain,int index,String unSignMsg);

}
