package com.labs339.platform;

import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.req.WithdrawReq;

public interface TransactionService {

    CommonResponse Withdraw(WithdrawReq req);

}
