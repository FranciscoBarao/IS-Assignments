package com.assign_1;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.shaded.io.netty.util.internal.SocketUtils;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class ProjectIsClient {
    private static final Logger logger = Logger.getLogger(ProjectIsClient.class.getName());

    private final ManagedChannel channel;
    private final ProjectIsGrpc.ProjectIsBlockingStub blockingStub;

    public ProjectIsClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    ProjectIsClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = ProjectIsGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void sendIDs(ArrayList<Integer> arr_id) {

        OwnersRequest request = OwnersRequest.newBuilder().addAllId(arr_id).build();

        Reply response;
        try {
            response = blockingStub.getCars(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }

        System.out.println("Response Arrived");
        ArrayList<Owner> owner_list = (ArrayList<Owner>) response.getOwnersList();

        for (Owner o : owner_list) {
            System.out.println(o.toString());
        }
    }

    public static void main(String[] args) throws Exception {
        ProjectIsClient client = new ProjectIsClient("localhost", 5682);
        try {
            ArrayList<Integer> arr = new ArrayList<>();
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++)
                    arr.add(Integer.parseInt(args[i]));
            }
            client.sendIDs(arr);
        } finally {
            client.shutdown();
        }
    }
}
