package com.labs339.platform.service.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.labs339.platform.AddressInfoService;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.dao.entity.WalletAddressModel;
import com.labs339.platform.service.AddressNonceService;
import com.labs339.platform.service.WalletAddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Wrapper;

@Slf4j
@DubboService(protocol = {"tri"})
public class AddressInfoServiceImpl implements AddressInfoService {

    @Autowired
    private WalletAddressService walletAddressService;
    @Autowired
    private AddressNonceService addressNonceService;

    @Override
    public CommonResponse GetUserAddress(Long userId, String chainName, String chainType) {
        return walletAddressService.GetUserAddress(userId, chainName, chainType);
    }

    @Override
    public CommonResponse GetAddressInfo(String address, String chainName, String chainType) {
        return null;
    }

    @Override
    public CommonResponse GetAddressUpdateNonce(String address,String chainName,Integer chainId) {
        return addressNonceService.GetAddressUpdateNonce(address,chainName,chainId);
    }
}
