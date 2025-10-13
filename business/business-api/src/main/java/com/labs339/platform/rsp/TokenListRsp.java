package com.labs339.platform.rsp;

import com.labs339.platform.baseDto.BaseChainInfo;
import com.labs339.platform.baseDto.BaseTokenInfo;
import lombok.Data;

import java.util.List;

@Data
public class TokenListRsp extends BaseTokenInfo {

    /**
     * token 支持的链
     */
    List<BaseChainInfo> chainInfoList;

}
