package com.projectis.assign_2;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
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

        // WHAT DOES THIS STEP DO?
        // ParameterExpression<String> p = builder.parameter(String.class);
        // WHat does setParameter do? -- Reusable queries?
        // query.setParameter(p, "query?");

        List<footballPlayer> results = query.getResultList();

        for (footballPlayer f : results) {
            System.out.println(f.toString());
        }
    }

    public void getFootballPlayersTallerHeight(EntityManager em, float height) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<footballPlayer> q = builder.createQuery(footballPlayer.class);
        Root<footballPlayer> root = q.from(footballPlayer.class);

        q.select(root).where(builder.gt(root.get("height"), height));

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

    public static void main(String[] args) {
        new App();
    }

    App() {
        /*
         * Entity manager implements API and encapsulates all of them within single
         * interface User to read,delete and write entities referenced objects are
         * managed by the entity manager
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProjectPersistence");
        EntityManager em = emf.createEntityManager();
        addFootballPlayers(em);
        // getFootballPlayersGivenPosition(em, "Quarterback");
        getFootballPlayersTallerHeight(em, 170);
    }
}
