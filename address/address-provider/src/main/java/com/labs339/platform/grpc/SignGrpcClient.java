package com.labs339.platform.grpc;

import com.labs339.platform.sign.*;
import io.grpc.ConnectivityState;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class SignGrpcClient implements DisposableBean {

    private final ManagedChannel channel;
    private final WalletSignServiceGrpc.WalletSignServiceBlockingStub stub;
    private final String host;
    private final int port;

    public SignGrpcClient(@Value("${grpc.server.host}") String host,
                          @Value("${grpc.server.port}") int port) {
        this.host = host;
        this.port = port;
        this.channel = createChannel();
        this.stub = WalletSignServiceGrpc.newBlockingStub(channel);
    }

    private ManagedChannel createChannel() {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(10, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .build();
    }

    public boolean isChannelHealthy() {
        ConnectivityState state = channel.getState(false);
        return state == ConnectivityState.READY || state == ConnectivityState.IDLE;
    }

    // 获取支持的签名方式
    public SupportSignWayResponse getSupportSignWay(String chain) {
        SupportSignWayRequest request = SupportSignWayRequest.newBuilder()
                .setChain(chain)
                .build();
        try {
            return stub.getSupportSignWay(request);
        } catch (StatusRuntimeException e) {
            System.out.println("Failed to call getSupportSignWay: " + e.getStatus());
            return null;
        }
    }

    // 导出公钥列表
    public ExportPublicKeyResponse exportPublicKeyList(String chain, long cursor, long size) {
        ExportPublicKeyRequest request = ExportPublicKeyRequest.newBuilder()
                .setWallet("0")
                .setChain(chain)
                .setCursor(cursor)
                .setSize(size)
                .build();
        try {
            return stub.exportPublicKeyList(request);
        } catch (StatusRuntimeException e) {
            System.out.println("Failed to call exportPublicKeyList: " + e.getStatus());
            return null;
        }
    }

    @Override
    public void destroy() {
        shutdown();
    }

    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                channel.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }


}
