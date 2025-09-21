package com.labs339.platform.task;


import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NodeBlockSyncHandler {

    /**
     * ETH扫链
     * @throws Exception
     */
    @XxlJob("ethNodeBlockSyncHandler")
    public void ethNodeBlockSyncHandler() throws Exception {

    }

    /**
     * ARB 扫链
     * @throws Exception
     */
    @XxlJob("arbNodeBlockSyncHandler")
    public void arbNodeBlockSyncHandler() throws Exception {

    }

    /**
     * Op 扫链
     * @throws Exception
     */
    @XxlJob("opNodeBlockSyncHandler")
    public void opNodeBlockSyncHandler() throws Exception {

    }

}
