package com.assign_1;

// To run:
// mvn exec:java -Dexec.mainClass=com.assign_1.ProjectIsClient

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import com.assign_1.*;

public class ProjectIsClient {
    private static final Logger logger = Logger.getLogger(ProjectIsClient.class.getName());

    private static int numberRequest = 5;
    private static boolean isXML = true;

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

        if (isXML) {
            OwnersRequest request = OwnersRequest.newBuilder().addAllId(arr_id).build();
            XML response = null;

            try {
                response = blockingStub.getCarsXml(request);
            } catch (StatusRuntimeException e) {
                logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            }
            System.out.println("Response Arrived");
            String sXml = response.getXmlString();

            ObjectToXml.reverse(sXml);

        } else {
            OwnersRequest request = OwnersRequest.newBuilder().addAllId(arr_id).build();
            Reply response;

            try {
                response = blockingStub.getCars(request);
            } catch (StatusRuntimeException e) {
                logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
                return;
            }

            System.out.println("Response Arrived");
            List<O> owner_list = response.getOwnersList();

            for (O o : owner_list) {
                System.out.println(o.getId() + " " + o.getName() + " " + o.getTelephone() + " " + o.getAddress());
                System.out.println("Cars:\n");
                System.out.println("-> " + o.getCarsCount());
                for (C c : o.getCarsList()) {
                    System.out.println(c.getId() + " " + c.getBrand() + " " + c.getModel() + " " + c.getEngineSize()
                            + " " + c.getConsumption() + " " + c.getPlate());
                }
            }
        }
        long endTime = System.currentTimeMillis();
        timeToFile(endTime);
    }

    public static void timeToFile(long endTime) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("out/endTime.txt")));
            // long convert = TimeUnit.SECONDS.convert(endTime, TimeUnit.NANOSECONDS);
            writer.write("" + endTime);
            writer.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        ProjectIsClient client = new ProjectIsClient("localhost", 5682);
        try {
            ArrayList<Integer> arr = new ArrayList<>();
            for (int i = 0; i < numberRequest; i++) {
                arr.add(i);
            }
            client.sendIDs(arr);
        } finally {
            client.shutdown();
        }
    }
}
