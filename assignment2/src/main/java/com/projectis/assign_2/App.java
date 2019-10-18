package com.projectis.assign_2;

// mvn exec:java -Dexec.mainClass=com.projectis.assign_2.App

import java.util.List;

import com.projectis.assign_2.*;

import java.sql.*;
import java.io.*;
import javax.persistence.*;
import java.util.ArrayList;

public class App {
    public static void main(String[] args) {
        // Dafuq is this
        // List<footballPlayer> mylist = GetFootballPlayerInfo.get();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/?user=" + "root");
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + "footballPlayer");
        } catch (Exception e) {
            System.out.println(e);
        }
        /*
         * Entity manager implements API and encapsulates all of them within single
         * interface User to read,delete and write entities referenced objects are
         * managed by the entity manager
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjectPersistence");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        ArrayList<footballPlayer> mylist = new ArrayList<>();
        footballPlayer f = new footballPlayer("joao", "1-1-2000", "Quarterback", 70);
        mylist.add(f);
        f = new footballPlayer("alex", "1-1-2000", "Quarterback", 70);
        mylist.add(f);
        f = new footballPlayer("belchior", "1-1-2000", "Quarterback", 70);
        mylist.add(f);

        tx.begin();
        for (footballPlayer fp : mylist) {
            em.persist(fp);
        }

        tx.commit();
    }
}
