package com.assign_1;

// To run:
// mvn exec:java -Dexec.mainClass=com.assign_1.ProjectIsServer

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import com.assign_1.*;

public class ProjectIsServer {
    private static final Logger logger = Logger.getLogger(ProjectIsServer.class.getName());

    // Parameters
    private static int numberOwners = 100000;
    private static int numberCars = 10;
    private static int port = 7000;

    // Random Name generator info
    final static String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    final static java.util.Random rand = new java.util.Random();
    final static Set<String> identifiers = new HashSet<String>();

    private Server server;
    public static ArrayList<Owner> owners;

    public void start() throws IOException {
        System.out.println("start");
        server = ServerBuilder.forPort(port).addService(new ProjectIsImpl()).build().start();

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

    public static void inDB() {
        try {
            FileInputStream fis = new FileInputStream("database.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            owners = (ArrayList<Owner>) ois.readObject();

            ois.close();
            fis.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void outDB() {
        try {
            FileOutputStream fos = new FileOutputStream("database.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(owners);
            oos.close();
            fos.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static String randomIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (builder.toString().length() == 0) {
            int length = rand.nextInt(5) + 5;
            for (int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if (identifiers.contains(builder.toString())) {
                builder = new StringBuilder();
            }
        }
        return builder.toString();
    }

    public static void generateList(int nOwners, int numberCars) {
        int nCars = 1;
        for (int i = 0; i < nOwners; i++) {
            String name = randomIdentifier();
            Owner o = new Owner(i, name, 1, "Street");

            for (int j = 0; j < numberCars; j++) {
                Car c = new Car(o, nCars, "brand", "model", 1, 1, 1, "10-UC-10");
                nCars++;
                o.addCars(c);
            }
            owners.add(o);
        }
    }

    public static void timeToFile(long startTime) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("out/startTime.txt", true));
            PrintWriter out = new PrintWriter(writer);
            // long convert = TimeUnit.SECONDS.convert(startTime, TimeUnit.NANOSECONDS);
            out.println("" + startTime);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void infoToFile(int numberOwners, int numberCars) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("out/info.txt", true));
            PrintWriter out = new PrintWriter(writer);
            // long convert = TimeUnit.SECONDS.convert(startTime, TimeUnit.NANOSECONDS);
            out.println("" + numberOwners + " " + numberCars);
            out.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void main(String[] args) throws Exception {
        ProjectIsServer server = new ProjectIsServer();
        owners = new ArrayList<Owner>();
        generateList(numberOwners, numberCars);
        outDB();
        try {
            // inDB();
            server.start();
            server.blockUntilShutdown();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public class ProjectIsImpl extends ProjectIsGrpc.ProjectIsImplBase {

        @Override
        public void getCarsXml(OwnersRequest request, StreamObserver<XML> responseObserver) {
            responseObserver.onNext(makeResponseXml(request));
            responseObserver.onCompleted();
        }

        private XML makeResponseXml(OwnersRequest id_list) {
            List<Integer> owners = id_list.getIdList();
            ArrayList<Owner> own = new ArrayList<>();

            long startTime = System.currentTimeMillis();
            timeToFile(startTime);

            for (int i : owners) {
                Owner o = findOwner(i);
                if (o != null)
                    own.add(o);
            }
            String s = "";
            try {
                s = ObjectToXml.transform(own);
            } catch (Exception e) {
                System.out.println(e);
            }
            return XML.newBuilder().setXmlString(s).build();
        }

        @Override
        public void getCars(OwnersRequest request, StreamObserver<Reply> responseObserver) {
            responseObserver.onNext(makeResponse(request));
            responseObserver.onCompleted();
        }

        private Reply makeResponse(OwnersRequest id_list) {
            List<Integer> owners = id_list.getIdList();
            ArrayList<O> reply_owners = new ArrayList<>();
            Reply rep;

            long startTime = System.currentTimeMillis();
            timeToFile(startTime);
            infoToFile(numberOwners, numberCars);
            for (int i : owners) {
                Owner o = findOwner(i);
                ArrayList<C> cs = new ArrayList<>();
                o.getCars()
                        .forEach((car) -> cs.add(C.newBuilder().setId(car.getId()).setBrand(car.getBrand())
                                .setModel(car.getModel()).setEngineSize(car.getEngine_size()).setPower(car.getPower())
                                .setConsumption(car.getConsumption()).setPlate(car.getPlate()).build()));
                reply_owners.add(O.newBuilder().setId(o.getId()).setName(o.getName()).setTelephone(o.getTelephone())
                        .setAddress(o.getAddress()).addAllCars(cs).build());
            }

            if (reply_owners.size() > 0)
                rep = Reply.newBuilder().addAllOwners(reply_owners).build();
            else
                rep = Reply.newBuilder().build();

            return rep;
        }

        private Owner findOwner(int id) {

            for (Owner o : owners) {
                if (o.getId() == id) {
                    return o;
                }
            }
            return null;
        }

    }
}
