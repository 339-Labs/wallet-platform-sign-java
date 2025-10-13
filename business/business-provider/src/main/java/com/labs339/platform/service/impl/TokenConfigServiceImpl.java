package com.labs339.platform.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.labs339.platform.baseDto.BaseChainInfo;
import com.labs339.platform.baseDto.BaseTokenInfo;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.config.ChainInfoConfig;
import com.labs339.platform.dao.entity.ChainConfigModel;
import com.labs339.platform.dao.entity.TokenConfigModel;
import com.labs339.platform.dao.mapper.TokenConfigMapper;
import com.labs339.platform.rsp.TokenListRsp;
import com.labs339.platform.service.TokenConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TokenConfigServiceImpl extends ServiceImpl<TokenConfigMapper, TokenConfigModel> implements TokenConfigService {

    @Autowired
    private ChainInfoConfig chainInfoConfig;

    @Override
    public CommonResponse GetTokenList() {
        List<BaseTokenInfo> tokenListRspList = new ArrayList<>();

        chainInfoConfig.getTokenChainMap().forEach((k, v) -> {
            TokenConfigModel tokenConfigModel = v.get(0);
            BaseTokenInfo tokenInfo = new BaseTokenInfo();
            tokenInfo.setTokenName(tokenConfigModel.getTokenName());
            tokenInfo.setTokenFullName(tokenConfigModel.getTokenFullName());
            tokenInfo.setTokenDecimal(tokenConfigModel.getTokenDecimal());
            tokenListRspList.add(tokenInfo);
        });

        return CommonResponse.success(tokenListRspList);
    }

    @Override
    public CommonResponse GetTokenChainList() {

        List<TokenListRsp> tokenListRspList = new ArrayList<>();
         chainInfoConfig.getTokenChainMap().forEach((k, v) -> {

             TokenListRsp tokenListRsp = new TokenListRsp();
             List<BaseChainInfo> chainInfos = new ArrayList<>();
             v.forEach(tokenConfigModel -> {
                 ChainConfigModel chainConfigModel = chainInfoConfig.getChainConfigMap().get(tokenConfigModel.getChainName());
                 BaseChainInfo baseChainInfo = ChainConfigModel.toChainRsp(chainConfigModel);
                 chainInfos.add(baseChainInfo);
             });
             tokenListRsp.setChainInfoList(chainInfos);
             tokenListRsp.setTokenName(v.get(0).getTokenName());
             tokenListRsp.setTokenFullName(v.get(0).getTokenFullName());
             tokenListRsp.setTokenDecimal(v.get(0).getTokenDecimal());
             tokenListRspList.add(tokenListRsp);
        });
        return CommonResponse.success(tokenListRspList);
    }





}
