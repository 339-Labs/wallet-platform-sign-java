package com.labs339.platform.task;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddressPoolHandler {

    /**
     * 监控地址池，并且新增
     * @throws Exception
     */
    @XxlJob("updateAddressPoll")
    public void updateAddressPoll() throws Exception {

    }

}
