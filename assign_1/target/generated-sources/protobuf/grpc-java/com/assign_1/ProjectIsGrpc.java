package com.assign_1;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * Define Service
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.24.0)",
    comments = "Source: project_is.proto")
public final class ProjectIsGrpc {

  private ProjectIsGrpc() {}

  public static final String SERVICE_NAME = "com.assign_1.ProjectIs";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.assign_1.OwnersRequest,
      com.assign_1.Reply> getGetCarsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getCars",
      requestType = com.assign_1.OwnersRequest.class,
      responseType = com.assign_1.Reply.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.assign_1.OwnersRequest,
      com.assign_1.Reply> getGetCarsMethod() {
    io.grpc.MethodDescriptor<com.assign_1.OwnersRequest, com.assign_1.Reply> getGetCarsMethod;
    if ((getGetCarsMethod = ProjectIsGrpc.getGetCarsMethod) == null) {
      synchronized (ProjectIsGrpc.class) {
        if ((getGetCarsMethod = ProjectIsGrpc.getGetCarsMethod) == null) {
          ProjectIsGrpc.getGetCarsMethod = getGetCarsMethod =
              io.grpc.MethodDescriptor.<com.assign_1.OwnersRequest, com.assign_1.Reply>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getCars"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.assign_1.OwnersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.assign_1.Reply.getDefaultInstance()))
              .setSchemaDescriptor(new ProjectIsMethodDescriptorSupplier("getCars"))
              .build();
        }
      }
    }
    return getGetCarsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ProjectIsStub newStub(io.grpc.Channel channel) {
    return new ProjectIsStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ProjectIsBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new ProjectIsBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ProjectIsFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new ProjectIsFutureStub(channel);
  }

  /**
   * <pre>
   * Define Service
   * </pre>
   */
  public static abstract class ProjectIsImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Request - Reply service
     * </pre>
     */
    public void getCars(com.assign_1.OwnersRequest request,
        io.grpc.stub.StreamObserver<com.assign_1.Reply> responseObserver) {
      asyncUnimplementedUnaryCall(getGetCarsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetCarsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.assign_1.OwnersRequest,
                com.assign_1.Reply>(
                  this, METHODID_GET_CARS)))
          .build();
    }
  }

  /**
   * <pre>
   * Define Service
   * </pre>
   */
  public static final class ProjectIsStub extends io.grpc.stub.AbstractStub<ProjectIsStub> {
    private ProjectIsStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ProjectIsStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProjectIsStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ProjectIsStub(channel, callOptions);
    }

    /**
     * <pre>
     * Request - Reply service
     * </pre>
     */
    public void getCars(com.assign_1.OwnersRequest request,
        io.grpc.stub.StreamObserver<com.assign_1.Reply> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetCarsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Define Service
   * </pre>
   */
  public static final class ProjectIsBlockingStub extends io.grpc.stub.AbstractStub<ProjectIsBlockingStub> {
    private ProjectIsBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ProjectIsBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProjectIsBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ProjectIsBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Request - Reply service
     * </pre>
     */
    public com.assign_1.Reply getCars(com.assign_1.OwnersRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetCarsMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Define Service
   * </pre>
   */
  public static final class ProjectIsFutureStub extends io.grpc.stub.AbstractStub<ProjectIsFutureStub> {
    private ProjectIsFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private ProjectIsFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ProjectIsFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new ProjectIsFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Request - Reply service
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.assign_1.Reply> getCars(
        com.assign_1.OwnersRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetCarsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_CARS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ProjectIsImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ProjectIsImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_CARS:
          serviceImpl.getCars((com.assign_1.OwnersRequest) request,
              (io.grpc.stub.StreamObserver<com.assign_1.Reply>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class ProjectIsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ProjectIsBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.assign_1.ProjectIsProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ProjectIs");
    }
  }

  private static final class ProjectIsFileDescriptorSupplier
      extends ProjectIsBaseDescriptorSupplier {
    ProjectIsFileDescriptorSupplier() {}
  }

  private static final class ProjectIsMethodDescriptorSupplier
      extends ProjectIsBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ProjectIsMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ProjectIsGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ProjectIsFileDescriptorSupplier())
              .addMethod(getGetCarsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
