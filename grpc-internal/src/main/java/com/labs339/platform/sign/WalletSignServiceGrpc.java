package com.labs339.platform.sign;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * WalletSignService gRPC service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler",
    comments = "Source: wallet_sign.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class WalletSignServiceGrpc {

  private WalletSignServiceGrpc() {}

  public static final String SERVICE_NAME = "labs339.wallet.sign.WalletSignService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.labs339.platform.sign.SupportSignWayRequest,
      com.labs339.platform.sign.SupportSignWayResponse> getGetSupportSignWayMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getSupportSignWay",
      requestType = com.labs339.platform.sign.SupportSignWayRequest.class,
      responseType = com.labs339.platform.sign.SupportSignWayResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.labs339.platform.sign.SupportSignWayRequest,
      com.labs339.platform.sign.SupportSignWayResponse> getGetSupportSignWayMethod() {
    io.grpc.MethodDescriptor<com.labs339.platform.sign.SupportSignWayRequest, com.labs339.platform.sign.SupportSignWayResponse> getGetSupportSignWayMethod;
    if ((getGetSupportSignWayMethod = WalletSignServiceGrpc.getGetSupportSignWayMethod) == null) {
      synchronized (WalletSignServiceGrpc.class) {
        if ((getGetSupportSignWayMethod = WalletSignServiceGrpc.getGetSupportSignWayMethod) == null) {
          WalletSignServiceGrpc.getGetSupportSignWayMethod = getGetSupportSignWayMethod =
              io.grpc.MethodDescriptor.<com.labs339.platform.sign.SupportSignWayRequest, com.labs339.platform.sign.SupportSignWayResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getSupportSignWay"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.labs339.platform.sign.SupportSignWayRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.labs339.platform.sign.SupportSignWayResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getGetSupportSignWayMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.labs339.platform.sign.ExportPublicKeyRequest,
      com.labs339.platform.sign.ExportPublicKeyResponse> getExportPublicKeyListMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "exportPublicKeyList",
      requestType = com.labs339.platform.sign.ExportPublicKeyRequest.class,
      responseType = com.labs339.platform.sign.ExportPublicKeyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.labs339.platform.sign.ExportPublicKeyRequest,
      com.labs339.platform.sign.ExportPublicKeyResponse> getExportPublicKeyListMethod() {
    io.grpc.MethodDescriptor<com.labs339.platform.sign.ExportPublicKeyRequest, com.labs339.platform.sign.ExportPublicKeyResponse> getExportPublicKeyListMethod;
    if ((getExportPublicKeyListMethod = WalletSignServiceGrpc.getExportPublicKeyListMethod) == null) {
      synchronized (WalletSignServiceGrpc.class) {
        if ((getExportPublicKeyListMethod = WalletSignServiceGrpc.getExportPublicKeyListMethod) == null) {
          WalletSignServiceGrpc.getExportPublicKeyListMethod = getExportPublicKeyListMethod =
              io.grpc.MethodDescriptor.<com.labs339.platform.sign.ExportPublicKeyRequest, com.labs339.platform.sign.ExportPublicKeyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "exportPublicKeyList"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.labs339.platform.sign.ExportPublicKeyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.labs339.platform.sign.ExportPublicKeyResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getExportPublicKeyListMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.labs339.platform.sign.SignTxMessageRequest,
      com.labs339.platform.sign.SignTxMessageResponse> getSignTxMessageMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "signTxMessage",
      requestType = com.labs339.platform.sign.SignTxMessageRequest.class,
      responseType = com.labs339.platform.sign.SignTxMessageResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.labs339.platform.sign.SignTxMessageRequest,
      com.labs339.platform.sign.SignTxMessageResponse> getSignTxMessageMethod() {
    io.grpc.MethodDescriptor<com.labs339.platform.sign.SignTxMessageRequest, com.labs339.platform.sign.SignTxMessageResponse> getSignTxMessageMethod;
    if ((getSignTxMessageMethod = WalletSignServiceGrpc.getSignTxMessageMethod) == null) {
      synchronized (WalletSignServiceGrpc.class) {
        if ((getSignTxMessageMethod = WalletSignServiceGrpc.getSignTxMessageMethod) == null) {
          WalletSignServiceGrpc.getSignTxMessageMethod = getSignTxMessageMethod =
              io.grpc.MethodDescriptor.<com.labs339.platform.sign.SignTxMessageRequest, com.labs339.platform.sign.SignTxMessageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "signTxMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.labs339.platform.sign.SignTxMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.labs339.platform.sign.SignTxMessageResponse.getDefaultInstance()))
              .build();
        }
      }
    }
    return getSignTxMessageMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WalletSignServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletSignServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletSignServiceStub>() {
        @Override
        public WalletSignServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletSignServiceStub(channel, callOptions);
        }
      };
    return WalletSignServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WalletSignServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletSignServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletSignServiceBlockingStub>() {
        @Override
        public WalletSignServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletSignServiceBlockingStub(channel, callOptions);
        }
      };
    return WalletSignServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WalletSignServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<WalletSignServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<WalletSignServiceFutureStub>() {
        @Override
        public WalletSignServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new WalletSignServiceFutureStub(channel, callOptions);
        }
      };
    return WalletSignServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * WalletSignService - Base service interface
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void getSupportSignWay(com.labs339.platform.sign.SupportSignWayRequest request,
        io.grpc.stub.StreamObserver<com.labs339.platform.sign.SupportSignWayResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetSupportSignWayMethod(), responseObserver);
    }

    /**
     */
    default void exportPublicKeyList(com.labs339.platform.sign.ExportPublicKeyRequest request,
        io.grpc.stub.StreamObserver<com.labs339.platform.sign.ExportPublicKeyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExportPublicKeyListMethod(), responseObserver);
    }

    /**
     */
    default void signTxMessage(com.labs339.platform.sign.SignTxMessageRequest request,
        io.grpc.stub.StreamObserver<com.labs339.platform.sign.SignTxMessageResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSignTxMessageMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service WalletSignService.
   */
  public static abstract class WalletSignServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return WalletSignServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service WalletSignService.
   */
  public static final class WalletSignServiceStub
      extends io.grpc.stub.AbstractAsyncStub<WalletSignServiceStub> {
    private WalletSignServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected WalletSignServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletSignServiceStub(channel, callOptions);
    }

    /**
     */
    public void getSupportSignWay(com.labs339.platform.sign.SupportSignWayRequest request,
        io.grpc.stub.StreamObserver<com.labs339.platform.sign.SupportSignWayResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetSupportSignWayMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void exportPublicKeyList(com.labs339.platform.sign.ExportPublicKeyRequest request,
        io.grpc.stub.StreamObserver<com.labs339.platform.sign.ExportPublicKeyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExportPublicKeyListMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void signTxMessage(com.labs339.platform.sign.SignTxMessageRequest request,
        io.grpc.stub.StreamObserver<com.labs339.platform.sign.SignTxMessageResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSignTxMessageMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service WalletSignService.
   */
  public static final class WalletSignServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<WalletSignServiceBlockingStub> {
    private WalletSignServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected WalletSignServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletSignServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.labs339.platform.sign.SupportSignWayResponse getSupportSignWay(com.labs339.platform.sign.SupportSignWayRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetSupportSignWayMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.labs339.platform.sign.ExportPublicKeyResponse exportPublicKeyList(com.labs339.platform.sign.ExportPublicKeyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExportPublicKeyListMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.labs339.platform.sign.SignTxMessageResponse signTxMessage(com.labs339.platform.sign.SignTxMessageRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSignTxMessageMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service WalletSignService.
   */
  public static final class WalletSignServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<WalletSignServiceFutureStub> {
    private WalletSignServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected WalletSignServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new WalletSignServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.labs339.platform.sign.SupportSignWayResponse> getSupportSignWay(
        com.labs339.platform.sign.SupportSignWayRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetSupportSignWayMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.labs339.platform.sign.ExportPublicKeyResponse> exportPublicKeyList(
        com.labs339.platform.sign.ExportPublicKeyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExportPublicKeyListMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.labs339.platform.sign.SignTxMessageResponse> signTxMessage(
        com.labs339.platform.sign.SignTxMessageRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSignTxMessageMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_SUPPORT_SIGN_WAY = 0;
  private static final int METHODID_EXPORT_PUBLIC_KEY_LIST = 1;
  private static final int METHODID_SIGN_TX_MESSAGE = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_SUPPORT_SIGN_WAY:
          serviceImpl.getSupportSignWay((com.labs339.platform.sign.SupportSignWayRequest) request,
              (io.grpc.stub.StreamObserver<com.labs339.platform.sign.SupportSignWayResponse>) responseObserver);
          break;
        case METHODID_EXPORT_PUBLIC_KEY_LIST:
          serviceImpl.exportPublicKeyList((com.labs339.platform.sign.ExportPublicKeyRequest) request,
              (io.grpc.stub.StreamObserver<com.labs339.platform.sign.ExportPublicKeyResponse>) responseObserver);
          break;
        case METHODID_SIGN_TX_MESSAGE:
          serviceImpl.signTxMessage((com.labs339.platform.sign.SignTxMessageRequest) request,
              (io.grpc.stub.StreamObserver<com.labs339.platform.sign.SignTxMessageResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetSupportSignWayMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.labs339.platform.sign.SupportSignWayRequest,
              com.labs339.platform.sign.SupportSignWayResponse>(
                service, METHODID_GET_SUPPORT_SIGN_WAY)))
        .addMethod(
          getExportPublicKeyListMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.labs339.platform.sign.ExportPublicKeyRequest,
              com.labs339.platform.sign.ExportPublicKeyResponse>(
                service, METHODID_EXPORT_PUBLIC_KEY_LIST)))
        .addMethod(
          getSignTxMessageMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.labs339.platform.sign.SignTxMessageRequest,
              com.labs339.platform.sign.SignTxMessageResponse>(
                service, METHODID_SIGN_TX_MESSAGE)))
        .build();
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (WalletSignServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getGetSupportSignWayMethod())
              .addMethod(getExportPublicKeyListMethod())
              .addMethod(getSignTxMessageMethod())
              .build();
        }
      }
    }
    return result;
  }
}
