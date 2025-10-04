package com.labs339.platform.service;

import com.labs339.platform.enums.AlgorithmType;

import java.util.List;

public interface WalletService {

    List generateKeyGen(String chain,List<Integer> indexs, AlgorithmType algorithm);

    String sign(String chain,int index,AlgorithmType algorithm,String unSignMsg);

}
