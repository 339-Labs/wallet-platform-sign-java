package com.labs339.platform.service;

import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.enums.AlgorithmType;

import java.util.List;

public interface WalletService {

    Boolean getSupportSignWay(String chainFullName);

    List<KeyPair> generateKeyGen(String chainFullName, Integer cursor, Integer size);

    String sign(String chainFullName,int index,String unSignMsg);

}
