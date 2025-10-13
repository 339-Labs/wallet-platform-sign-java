package com.labs339.platform.service.api;

import com.labs339.platform.TransactionService;
import com.labs339.platform.common.CommonResponse;
import com.labs339.platform.req.WithdrawReq;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Slf4j
@DubboService(protocol = {"rest"})
public class TransactionServiceImpl implements TransactionService {

    @Override
    @POST
    @Path("/withdraw")
    public CommonResponse Withdraw(WithdrawReq req) {
        return null;
    }
}
