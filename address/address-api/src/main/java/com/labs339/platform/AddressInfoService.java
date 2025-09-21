package com.labs339.platform;

import com.labs339.platform.common.CommonResponse;

public interface AddressInfoService {

    CommonResponse GetUserAddress(Long userId,String chainName,String chainType);

    CommonResponse GetAddressInfo(String address,String chainName,String chainType);

    CommonResponse GetAddressUpdateNonce();

}


