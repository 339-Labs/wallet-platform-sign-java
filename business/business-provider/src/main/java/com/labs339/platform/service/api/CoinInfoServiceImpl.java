package com.labs339.platform.service.api;

import com.labs339.platform.CoinInfoService;
import com.labs339.platform.baseDto.BaseChainInfo;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.config.ChainInfoConfig;
import com.labs339.platform.service.ChainConfigService;
import com.labs339.platform.service.TokenConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Slf4j
@DubboService(protocol = {"rest"})
public class CoinInfoServiceImpl implements CoinInfoService {

    @Autowired
    private ChainInfoConfig chainInfoConfig;
    @Autowired
    private TokenConfigService tokenConfigService;
    @Autowired
    private ChainConfigService chainConfigService;

    @Override
    @GET
    @Path("/refreshConfig")
    public CommonResponse RefreshConfig() {
        chainInfoConfig.refresh();
        // todo 其他服务配置刷新
        return CommonResponse.success();
    }

    @Override
    @GET
    @Path("/getChainList")
    public CommonResponse GetChainList() {
        return chainConfigService.GetChainList();
    }

    @Override
    @GET
    @Path("/getTokenList")
    public CommonResponse GetTokenList(BaseChainInfo chainInfo) {
        return tokenConfigService.GetTokenList();
    }

    @Override
    @GET
    @Path("/getTokenChainList")
    public CommonResponse GetTokenChainList(BaseChainInfo chainInfo) {
        return tokenConfigService.GetTokenChainList();
    }
}
