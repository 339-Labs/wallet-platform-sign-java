package com.labs339.platform;

import com.labs339.platform.common.CommonResponse;

public interface UserInfoService {

    CommonResponse GetUserToken(Long userId, String chainName, String chainType);

    CommonResponse InternalTransfer();

}
