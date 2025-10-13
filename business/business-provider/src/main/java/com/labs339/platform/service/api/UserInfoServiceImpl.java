package com.labs339.platform.service.api;

import com.labs339.platform.UserInfoService;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.req.DepositInfoReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Slf4j
@DubboService(protocol = {"rest"})
public class UserInfoServiceImpl implements UserInfoService {
    @Override
    @POST
    @Path("/getDepositAddress")
    public CommonResponse GetDepositAddress(DepositInfoReq req) {
        return null;
    }

    @Override
    @POST
    @Path("/getUserToken")
    public CommonResponse GetUserToken(Long userId) {
        return null;
    }
}
