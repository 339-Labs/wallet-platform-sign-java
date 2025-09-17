package com.labs339.platform;

import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.req.DepositInfoReq;

public interface UserInfoService {

    CommonResponse GetDepositAddress(DepositInfoReq req);

}
