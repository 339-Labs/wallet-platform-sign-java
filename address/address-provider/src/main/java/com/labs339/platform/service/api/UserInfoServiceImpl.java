package com.labs339.platform.service.api;

import com.labs339.platform.UserInfoService;
import com.labs339.platform.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

@Slf4j
@DubboService(protocol = {"tri"})
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    public CommonResponse GetUserToken(Long userId, String chainName, String chainType) {
        return null;
    }

    @Override
    public CommonResponse InternalTransfer() {
        return null;
    }
}
