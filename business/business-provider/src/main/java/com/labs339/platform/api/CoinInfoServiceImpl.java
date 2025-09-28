package com.labs339.platform.api;

import com.labs339.platform.CoinInfoService;
import com.labs339.platform.baseDto.BaseChainInfo;
import com.labs339.platform.common.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Slf4j
@DubboService(protocol = {"rest"})
public class CoinInfoServiceImpl implements CoinInfoService {
    @Override
    @GET
    @Path("/getChainList")
    public CommonResponse GetChainList() {
        return null;
    }

    @Override
    @GET
    @Path("/getTokenList")
    public CommonResponse GetTokenList(BaseChainInfo chainInfo) {
        return null;
    }
}
