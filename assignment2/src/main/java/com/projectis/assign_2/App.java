package com.projectis.assign_2;

import java.util.ArrayList;

// mvn exec:java -Dexec.mainClass=com.projectis.assign_2.App

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

public class App {

    public void getFootballPlayersGivenPosition(EntityManager em, String position) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<footballPlayer> q = builder.createQuery(footballPlayer.class);
        Root<footballPlayer> root = q.from(footballPlayer.class);

        // criteria builder has conditions in itself to create where clauses
        q.select(root).where(builder.equal(root.get("position"), position));

        // query create
        TypedQuery<footballPlayer> query = em.createQuery(q);

        List<footballPlayer> results = query.getResultList();

        for (footballPlayer f : results) {
            System.out.println(f.toString());
        }
    }

    public void getFootballPlayersTallerHeight(EntityManager em, float height) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<footballPlayer> q = builder.createQuery(footballPlayer.class);
        Root<footballPlayer> root = q.from(footballPlayer.class);

        Path<Integer> x = root.get("height");

        q.select(root).where(builder.gt(x, height));

        TypedQuery<footballPlayer> query = em.createQuery(q);
        List<footballPlayer> results = query.getResultList();

        for (footballPlayer f : results) {
            System.out.println(f.toString());
        }
    }

    public void addFootballPlayers(EntityManager em) {
        // Add things to table

        EntityTransaction tx = em.getTransaction();
        ArrayList<footballPlayer> mylist = new ArrayList<>();
        footballPlayer f = new footballPlayer("o", "1-1-2000", "Keeper", 200);
        mylist.add(f);
        f = new footballPlayer("a1", "1-1-2000", "Quarterback", 180);
        mylist.add(f);
        f = new footballPlayer("a2", "1-1-2000", "asd", 190);
        mylist.add(f);

        tx.begin();
        for (footballPlayer fp : mylist) {
            em.persist(fp);
        }
        tx.commit();
    }

    public void AddTeamsPlayers(EntityManager em) {
        // Add things to table

        EntityTransaction tx = em.getTransaction();
        ArrayList<footballPlayer> mylist = new ArrayList<>();

        List<footballPlayer> formerPlayersT1 = new ArrayList<>();
        List<footballPlayer> formerPlayersT2 = new ArrayList<>();

        Team t1 = new Team("Team1", "adress1", "president1");

        footballPlayer f = new footballPlayer("a1", "1-1-2000", "Keeper", 200);
        f.setTeam(t1);
        mylist.add(f);
        formerPlayersT2.add(f);

        f = new footballPlayer("a2", "1-1-2000", "Quarterback", 180);
        f.setTeam(t1);
        mylist.add(f);
        formerPlayersT2.add(f);

        f = new footballPlayer("a3", "1-1-2000", "asd", 190);
        f.setTeam(t1);
        mylist.add(f);

        Team t2 = new Team("team2", "address2", "president2");

        f = new footballPlayer("a4", "1-1-2000", "asd", 190);
        f.setTeam(t2);
        mylist.add(f);
        formerPlayersT1.add(f);

        f = new footballPlayer("a5", "1-1-2000", "asd", 190);
        f.setTeam(t2);
        mylist.add(f);
        formerPlayersT1.add(f);

        t1.setFormerPlayers(formerPlayersT1);
        t2.setFormerPlayers(formerPlayersT2);

        em.persist(t1);
        em.persist(t2);

        tx.begin();
        for (footballPlayer fp : mylist) {
            em.persist(fp);
        }
        tx.commit();

    }

    public void getTeamPlayers(EntityManager em) {
        String givenTeam = "Team1";

        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<footballPlayer> criteria = builder.createQuery(footballPlayer.class);
        Root<Team> root = criteria.from(Team.class);

        Join<Team, footballPlayer> fplayers = root.join("footballPlayers");
        criteria.select(fplayers).where(builder.equal(root.get("name"), givenTeam));

        TypedQuery<footballPlayer> query = em.createQuery(criteria);
        List<footballPlayer> results = query.getResultList();

        for (footballPlayer f : results) {
            System.out.println(f.toString());
        }
    }

    public static void main(String[] args) {
        new App();
    }

    App() {
        /*
         * Entity manager implements API and encapsulates all of them within single
         * interface User to read,delete and write entities referenced objects are
         * managed by the entity manager(
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjectPersistence");
        EntityManager em = emf.createEntityManager();
        // addFootballPlayers(em);
        // getFootballPlayersGivenPosition(em, "Quarterback");
        // getFootballPlayersTallerHeight(em, 170);
        AddTeamsPlayers(em);
        getTeamPlayers(em);
    }
}
