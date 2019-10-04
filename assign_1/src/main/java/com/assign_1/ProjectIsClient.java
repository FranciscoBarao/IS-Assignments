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
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.assign_1.*;

public class ProjectIsClient {
    private static final Logger logger = Logger.getLogger(ProjectIsClient.class.getName());

    private static boolean isXML = true;

    private final ManagedChannel channel;
    private final ProjectIsGrpc.ProjectIsBlockingStub blockingStub;

    public ProjectIsClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().maxInboundMessageSize(1000000000).build());
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
            sizeToFile(response.getSerializedSize());

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
            sizeToFile(response.getSerializedSize());
        }
        long endTime = System.currentTimeMillis();
        timeToFile(endTime);
        // Returns size of message
    }

    public static void timeToFile(long endTime) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("out/endTime.txt", true));
            PrintWriter out = new PrintWriter(writer);
            // long convert = TimeUnit.SECONDS.convert(endTime, TimeUnit.NANOSECONDS);
            out.println("" + endTime);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void sizeToFile(int size) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("out/size.txt", true));
            PrintWriter out = new PrintWriter(writer);
            // long convert = TimeUnit.SECONDS.convert(endTime, TimeUnit.NANOSECONDS);
            out.println("" + size);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        // int[] ar = { 50, 150, 300, 500, 1000, 3000, 5000, 7500, 10000 };
        int[] ar = { 10000 };
        for (int j = 0; j < ar.length; j++) {
            for (int z = 0; z < 3; z++) {
                runScript(ar[j]);
            }
        }
    }

    public static void runScript(int length) {
        ProjectIsClient client = new ProjectIsClient("localhost", 7000);

        try {
            ArrayList<Integer> arr = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                arr.add(i);
            }
            client.sendIDs(arr);
        } finally {
            try {
                client.shutdown();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }

}
