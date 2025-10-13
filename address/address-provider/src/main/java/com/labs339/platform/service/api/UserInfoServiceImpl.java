package com.labs339.platform.service.api;

import com.labs339.platform.UserInfoService;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.req.TransferTokenReq;
import com.labs339.platform.req.UserTokenReq;
import com.labs339.platform.service.WalletBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@DubboService(protocol = {"tri"})
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private WalletBalanceService walletBalanceService;

    @Override
    public CommonResponse GetUserToken(UserTokenReq req) {
        return walletBalanceService.GetUserToken(req);
    }

    @Override
    public CommonResponse TransferToken(TransferTokenReq req) {
        return null;
    }


}
