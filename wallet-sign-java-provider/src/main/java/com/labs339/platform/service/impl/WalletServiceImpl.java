package com.labs339.platform.service.impl;

import com.labs339.platform.enums.AlgorithmType;
import com.labs339.platform.service.WalletService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletServiceImpl implements WalletService {
    @Override
    public List generateKeyGen(String chain, List<Integer> indexs, AlgorithmType algorithm) {
        return List.of();
    }

    @Override
    public String sign(String chain, int index, AlgorithmType algorithm, String unSignMsg) {
        return "";
    }
}
