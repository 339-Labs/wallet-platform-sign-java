package com.labs339.platform.service;

import com.labs339.platform.enums.AlgorithmType;

import java.util.List;

public interface WalletService {

    List generateKeyGen(String chain,Integer cursor,Integer size);

    String sign(String chain,int index,String unSignMsg);

}
