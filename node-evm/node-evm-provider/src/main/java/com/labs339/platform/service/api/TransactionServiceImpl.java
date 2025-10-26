package com.labs339.platform.service.api;

import com.labs339.platform.TransactionService;
import com.labs339.platform.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService(protocol = {"tri"})
public class TransactionServiceImpl implements TransactionService {
    @Override
    public CommonResponse SendTransaction(String signData, String chainName) {
        return null;
    }

    @Override
    public CommonResponse GetTransactionByTxHash(String hash, String chainName) {
        return null;
    }

    @Override
    public CommonResponse GetAddressAmount(String address,String tokenAddress, String chainName) {
        return null;
    }

    @Override
    public CommonResponse GetLatestBlock(String chainName) {
        return null;
    }
}
