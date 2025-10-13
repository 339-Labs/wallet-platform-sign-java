package com.labs339.platform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.dao.entity.WalletBalanceModel;
import com.labs339.platform.req.UserTokenReq;

public interface WalletBalanceService extends IService<WalletBalanceModel> {

    CommonResponse GetUserToken(UserTokenReq req);


    CommonResponse UpdateWalletAmount(WalletBalanceModel model);

}
