package com.labs339.platform.service.api;

import com.labs339.platform.ChainInfoService;
import com.labs339.platform.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService(protocol = {"tri"})
public class ChainInfoServiceImpl implements ChainInfoService {
    @Override
    public CommonResponse CoinInfo() {
        return null;
    }

    @Override
    public CommonResponse GetAddressSequence(String address, String chainName) {
        return null;
    }
}
