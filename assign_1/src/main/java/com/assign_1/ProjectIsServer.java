package com.assign_1;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.assign_1.*;

public class ProjectIsServer {
    private static final Logger logger = Logger.getLogger(ProjectIsServer.class.getName());

    private Server server;
    public static ArrayList<Owner> owners;

    public void start() throws IOException {
        System.out.println("start");
        int port = 5682;
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

    public void inDB() {
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

    public void outDB() {
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

    public static void createList() {
        Owner o = new Owner(1, "Pedro", 123, "Rua Dos Buracos");
        Car c = new Car(o, 8, "bmw", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        c = new Car(o, 7, "bmw", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        o = new Owner(2, "Hobbit", 153, "Shire");
        c = new Car(o, 6, "mini", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        o = new Owner(3, "Gonzaga", 197, "Rua Sésamo");
        c = new Car(o, 5, "ola", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        c = new Car(o, 4, "ola", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        c = new Car(o, 3, "nocar", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        o = new Owner(4, "Duarte", 169, "FlagTown");
        c = new Car(o, 2, "lalala", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);

        o = new Owner(5, "Lucas", 188, "Madeira");
        c = new Car(o, 1, "Micra", "a1", 1, 1, 1, "AZ-8-TE");
        o.addCars(c);
        owners.add(o);
    }

    public static void main(String[] args) throws Exception {
        ProjectIsServer server = new ProjectIsServer();
        owners = new ArrayList<Owner>();
        createList();
        // outDB();
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
        public void getCars(OwnersRequest request, StreamObserver<Reply> responseObserver) {
            responseObserver.onNext(makeResponse(request));
            responseObserver.onCompleted();
        }

        private Reply makeResponse(OwnersRequest id_list) {
            ArrayList<Integer> owners = (ArrayList<Integer>) id_list.getIdList();
            ArrayList<O> reply_owners = new ArrayList<>();
            Reply rep;
            for (int i : owners) {
                Owner o = findOwner(i);
                reply_owners.add(O.newBuilder().setId(o.getId()).setName(o.getName()).setTelephone(o.getTelephone())
                        .setAddress(o.getAddress())

                        .build());
            }
            // Sera que funciona??
            if (reply_owners.size() > 0) {

                rep = Reply.newBuilder().addAllOwners(reply_owners).build();
            }

            return Reply.newBuilder().build();
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
