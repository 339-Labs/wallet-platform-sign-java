package com.labs339.platform;

import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.req.TransferTokenReq;
import com.labs339.platform.req.UserTokenReq;

public interface UserInfoService {

    CommonResponse GetUserToken(UserTokenReq req);

    CommonResponse TransferToken(TransferTokenReq req);

}
