package com.assign_1;

// TO RUN: 
// mvn exec:java -Dexec.mainClass=com.assign_1.ProjectIsClient

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectIsClient {
    //Not sure bout this "getName()"
  private static final Logger logger = Logger.getLogger(ProjectIsClient.class.getName());

  private final ManagedChannel channel;
  private final ProjectIsGrpc.ProjectIsBlockingStub blockingStub;

  public ProjectIsClient(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build());
  }

  ProjectIsClient(ManagedChannel channel) {
    this.channel = channel;
    blockingStub = ProjectIsGrpc.newBlockingStub(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }



  public void sendIDs(String ids) {
    logger.info("Sending IDs "+ ids);
    //!!!!!!!!!OLHAR PARA AQUI!!!!!!!!!
    /*Esta merda dos Ids e dos Arrays nao esta bem caralho*/
    OwnersRequest request = OwnersRequest.newBuilder().setId(Integer.parseInt(ids)).build();
    CarsReply response;
    try {
      response = blockingStub.getCars(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }
    logger.info("Response Arrived");
     /*Aqui ja contem o numero de carros por pessoa em responde
    logger.info("Greeting: " + response.getMessage());
    */
    logger.info("Answer: " + response.getNumber());    
}


  public static void main(String[] args) throws Exception {
    ProjectIsClient client = new ProjectIsClient("localhost", 50051);
    try {
        String ids = "5";
      if (args.length > 0) {
        ids = args[0]; //Get ids here
      }
      client.sendIDs(ids);
    } finally {
      client.shutdown();
    }
  }
}
