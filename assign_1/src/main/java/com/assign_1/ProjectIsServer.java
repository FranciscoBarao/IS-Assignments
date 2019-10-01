package com.assign_1;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.logging.Logger;

public class ProjectIsServer {
  private static final Logger logger = Logger.getLogger(ProjectIsServer.class.getName());

  private Server server;


  public void start() throws IOException {

    int port  = 50051;
    server = ServerBuilder.forPort(port)
        .addService(new ProjectIsImpl())
        .build()
        .start();
    logger.info("Server started, listening on " + port);
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        System.err.println("*** shutting down gRPC server since JVM is shutting down");
        ProjectIsServer.this.stop();
        System.err.println("*** server shut down");
      }
    });
  }

  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  private void blockUntilShutdown() throws InterruptedException {
    if (server != null) {
      server.awaitTermination();
    }
  }

  public static void main(String[] args) throws Exception {
    ProjectIsServer server = new ProjectIsServer();
    server.start();
    server.blockUntilShutdown();
  }

  
  private static class ProjectIsImpl extends ProjectIsGrpc.ProjectIsImplBase {

    @Override
    public void getCars(OwnersRequest request, StreamObserver<CarsReply> responseObserver) {
      responseObserver.onNext(checkCars(request));
      responseObserver.onCompleted();
    }
   
    private CarsReply checkCars(OwnersRequest id_list) {
      //for () {
        /*Server has received ID List
          Go count number of cars 
          Return number of cars of each one
          */ 
          logger.info("On checkCars");
          return CarsReply.newBuilder().setNumber(1).build();
      //}

      // No feature was found, return an unnamed feature.
      // !? IS THIS RIGHT ?!
      //return CarsReply.newBuilder().setN_cars(0).build(); 
    }

  }
}
