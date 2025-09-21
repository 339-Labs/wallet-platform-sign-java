package com.labs339.platform;

import com.labs339.platform.common.CommonResponse;

public interface ChainInfoService {

    CommonResponse CoinInfo();

    CommonResponse GetAddressSequence(String address,String chainName);

}
