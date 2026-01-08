package com.labs339.platform.grpc;

import com.labs339.platform.*;
import com.labs339.platform.dao.KeyPair;
import com.labs339.platform.service.WalletService;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
    public class WalletSignGrpc extends WalletSignServiceGrpc.WalletSignServiceImplBase {

    @Autowired
    private WalletService walletService;

    @Override
    public void getSupportSignWay(SupportSignWayRequest request, StreamObserver<SupportSignWayResponse> responseObserver) {
        walletService.getSupportSignWay(request.getChain());
    }

    @Override
    public void exportPublicKeyList(ExportPublicKeyRequest request, StreamObserver<ExportPublicKeyResponse> responseObserver) {

        int cursor= (int)request.getCursor();
        int size = (int)request.getSize();
        List<KeyPair> list = walletService.generateKeyGen(request.getChain(),cursor,size);

        List<PublicKey> publicKeys = list.stream().map(key->{
            PublicKey publicKey = PublicKey.newBuilder()
                    .setPublicKeyHex(key.getPublicKeyHex())
                    .setChain(key.getCoin())
                    .setIndex(key.getIndex())
                    .build();
            return publicKey;
        }).toList();
        ExportPublicKeyResponse response = ExportPublicKeyResponse.newBuilder()
                .setMsg("success")
                .setCode("0")
                .addAllPublicKey(publicKeys)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void signTxMessage(SignTxMessageRequest request, StreamObserver<SignTxMessageResponse> responseObserver) {

        int size = (int)request.getIndex();
        String sign = walletService.sign(request.getChain(), size,request.getUnSignMsg());
        SignTxMessageResponse response = SignTxMessageResponse.newBuilder()
                .setMsg("success")
                .setCode("0")
                .setSignature(sign)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
