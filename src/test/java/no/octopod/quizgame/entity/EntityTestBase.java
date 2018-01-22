package no.octopod.quizgame.entity;

import org.junit.After;
import org.junit.Before;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Consumer;

public class EntityTestBase {

    protected EntityManagerFactory emFactory;
    protected EntityManager em;

    @Before
    public void setUp() {
        emFactory = Persistence.createEntityManagerFactory("DB");
        em = emFactory.createEntityManager();
    }

    @After
    public void tearDown() {
        em.close();
        emFactory.close();
    }

    protected boolean persistTransaction(Object... objects) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            for (Object o : objects) {
                em.persist(o);
            }
            tx.commit();
        } catch (Exception e) {
            System.out.println("Transaction persist failure: " + e.getMessage());
            tx.rollback();
            return false;
        }
        return true;
    }

    protected boolean runTransaction(Consumer<EntityManager> emConsumer) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            emConsumer.accept(em);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Transaction failed: " + e.getMessage());
            tx.rollback();
            return false;
        }
        return true;
    }
}
