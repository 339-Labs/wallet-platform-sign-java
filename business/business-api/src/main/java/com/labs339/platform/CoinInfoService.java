package com.labs339.platform;

import com.labs339.platform.baseDto.BaseChainInfo;
import com.labs339.platform.common.CommonResponse;

public interface CoinInfoService {

    CommonResponse RefreshConfig();

    CommonResponse GetChainList();

    CommonResponse GetTokenList(BaseChainInfo chainInfo);

    CommonResponse GetTokenChainList(BaseChainInfo chainInfo);
}
