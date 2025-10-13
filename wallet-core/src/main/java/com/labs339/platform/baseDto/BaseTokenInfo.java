package com.labs339.platform.baseDto;

import lombok.Data;

@Data
public class BaseTokenInfo extends BaseChainInfo {

    private String tokenAddress;

    private String tokenName;

    private String tokenFullName;

    private boolean isOrigin;

    private Integer tokenDecimal;

}
