package com.labs339.platform.service.api;

import com.labs339.platform.AddressInfoService;
import com.labs339.platform.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService(protocol = {"tri"})
public class AddressInfoServiceImpl implements AddressInfoService {
    @Override
    public CommonResponse GetUserAddress(Long userId, String chainName, String chainType) {
        return null;
    }

    @Override
    public CommonResponse GetAddressInfo(String address, String chainName, String chainType) {
        return null;
    }

    @Override
    public CommonResponse GetAddressUpdateNonce() {
        return null;
    }
}
