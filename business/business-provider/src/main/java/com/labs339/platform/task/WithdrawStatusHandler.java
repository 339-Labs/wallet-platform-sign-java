package com.labs339.platform.task;


import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WithdrawStatusHandler  {


    /**
     * 风控处理
     * @return
     * @throws Exception
     */
    @XxlJob("handlerRisk")
    public ReturnT<String> handlerRisk() throws Exception{

        return ReturnT.ofFail("风控处理失败");
    }


    /**
     * 批量 构建交易 发到交易服务处理 form 地址
     * @throws Exception
     */
    @XxlJob("buildBatchTransaction")
    public void buildBatchTransaction() throws Exception {

    }

    /**
     * 提现成功通知
     * @throws Exception
     */
    @XxlJob("withdrawSuccessNotify")
    public void handlerWithdrawSuccessNotify() throws Exception {

    }

}
