package com.labs339.platform.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.labs339.platform.addressresolver.AddressResolverFactory;
import com.labs339.platform.addressresolver.AddressResolverStrategy;
import com.labs339.platform.dao.entity.AddressPoolModel;
import com.labs339.platform.enums.ChainType;
import com.labs339.platform.grpc.SignGrpcClient;
import com.labs339.platform.service.AddressPoolService;
import com.labs339.platform.sign.ExportPublicKeyResponse;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddressPoolHandler {

    @Autowired
    private AddressPoolService addressPoolService;
    @Autowired
    private SignGrpcClient signGrpcClient;

    /**
     * 监控地址池，并且新增
     * @throws Exception
     */
    @XxlJob("updateEvmAddressPoll")
    public void updateEvmAddressPoll() throws Exception {

        long size= 0L;
        long cursor= 0L;

        ChainType chainType = ChainType.Ether;

        Long count = addressPoolService.count(new LambdaQueryWrapper<AddressPoolModel>().eq(AddressPoolModel::getChainName,chainType.getChainName()).eq(AddressPoolModel::getUsed,false));
        if (count < 10) {

            log.info("Starting updateAddressPoll public keys task");
            try {
                // 检查连接健康状态
                if (!signGrpcClient.isChannelHealthy()) {
                    log.warn("gRPC channel is not healthy, skipping this execution");
                    return;
                }

                ExportPublicKeyResponse response = signGrpcClient.exportPublicKeyList(chainType.getChainName(), cursor, size);

                if (response != null) {log.info("Successfully exported {} public keys, cursor: {}", response.getPublicKeyCount(), cursor);

                    // 处理导出的公钥数据
                   response.getPublicKeyList().forEach(publicKey -> {
                       AddressResolverStrategy addressResolverStrategy = AddressResolverFactory.getAddressResolverStrategy(chainType);
                       String address = addressResolverStrategy.resolve(publicKey.getPublicKeyHex(),null);

                   });

                    // 更新游标
                    if (response.getPublicKeyCount() > 0) {

                    } else {
                        // 如果没有更多数据，重置游标
                        log.info("No more data, resetting cursor to 0");

                    }
                } else {
                    log.error("Failed to export public keys, response is null");
                }

            } catch (StatusRuntimeException e) {
                log.error("gRPC call failed: {}, Status: {}",
                        e.getMessage(), e.getStatus(), e);
                handleGrpcException(e);
            } catch (Exception e) {
                log.error("Unexpected error during export public keys task", e);
            }

        }

    }

    private void handleGrpcException(StatusRuntimeException e) {
        Status.Code code = e.getStatus().getCode();
        switch (code) {
            case UNAVAILABLE:
                log.error("gRPC server unavailable, will retry in next schedule");
                break;
            case DEADLINE_EXCEEDED:
                log.error("gRPC call timeout");
                break;
            case CANCELLED:
                log.error("gRPC call cancelled");
                break;
            default:
                log.error("gRPC error: {}", code);
        }
    }

}
