package io.grpc.examples.projectis;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectIsClient {
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



  public void sendIDs(int[] ids) {
    
    /*OwnersRequest.Builder b  = OwnersRequest.newBuilder();
    for(int i : ids){
        b.addId(i);
    }
    OwnersRequest request = b.build();
    */
    OwnersRequest request = OwnersRequest.newBuilder()
        .addAllId(ids)
        .build();

    Reply response;
    try {
      response = blockingStub.getCars(request);
    } catch (StatusRuntimeException e) {
      logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
      return;
    }

    System.out.println("Response Arrived");    
    String own[] = response.getNumber().split("\\|");
    for(String o : own){
        System.out.println("<--------------->");
      String c[] = o.split(" ");
      for(String s : c){
        System.out.println(s);
      }
    }
  }


  public static void main(String[] args) throws Exception {
    ProjectIsClient client = new ProjectIsClient("localhost", 5682);
    try {
      String ids = "";
      if (args.length > 0) {
        for(int i=0;i< args.length;i++)   
          ids += args[i] + "|"; //Get ids here
        ids = ids.substring(0, ids.length() - 1); //remove last "|"
      }
      client.sendIDs(ids);
    } finally {
      client.shutdown();
    }
  }
}
