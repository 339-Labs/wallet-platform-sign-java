package com.labs339.platform.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WithdrawSubmitHandler {

    /**
     * 批量 构建交易 发到交易服务处理 form 地址
     * @throws Exception
     */
    @XxlJob("buildBatchTransaction")
    public void buildBatchTransaction() throws Exception {

    }


}
