package no.octopod.quizgame.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class QuizTest {
    private EntityManagerFactory emFactory;
    private EntityManager em;

    @Before
    public void setUp() {
        emFactory = Persistence.createEntityManagerFactory("database");
        em = emFactory.createEntityManager();
    }

    @After
    public void tearDown() {
        em.close();
        emFactory.close();
    }

    @Test
    public void testPersistenceFailsOnInvalidId() {
        Quiz quiz = new Quiz();
        quiz.setId(1L);
        assertNotNull(quiz.getId());
        assertFalse(persist(quiz));
    }

    @Test
    public void testSuccessfulPersistenceOnNullId() {
        Quiz quiz = new Quiz();
        assertNull(quiz.getId());
        persist(quiz);
        assertNotNull(quiz.getId());
    }

    @Test
    public void testPersistenceFailsOnSecondObjectWithSameId() {
        Quiz quiz1 = new Quiz();
        Quiz quiz2 = new Quiz();
        quiz2.setId(1L);

        // quiz1 should be persisted and quiz2 should fail as duplicate id
        assertTrue(persist(quiz1));
        assertFalse(persist(quiz2));

    }

    @Test
    public void testPersistenceTwiceOnOneObject() {
        Quiz quiz = new Quiz();
        quiz.setQuestion("First question?");

        assertTrue(persist(quiz));
        quiz.setQuestion("Second questions?");

        assertTrue(persist(quiz));
    }

    @Test
    public void testWriteRead() {
        String firstQuestion = "first";
        String secondQuestion = "second";

        Quiz quiz1 = new Quiz();
        quiz1.setQuestion(firstQuestion);

        Quiz quiz2 = new Quiz();
        quiz2.setQuestion(secondQuestion);

        persist(quiz1);
        persist(quiz2);

        // read from db, not cache
        em.close();
        em = emFactory.createEntityManager();

        //query for data
        Query query = em.createQuery("SELECT k FROM Quiz k");
        List<Quiz> quizzes = query.getResultList();

        assertThat(quizzes.size(), is(2));
        assertTrue(quizzes.stream().anyMatch(k -> k.getQuestion().equals(firstQuestion)));
        assertTrue(quizzes.stream().anyMatch(k -> k.getQuestion().equals(secondQuestion)));
    }

    @SuppressWarnings("Duplicates")
    private boolean persist(Object obj) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(obj);
            tx.commit();

        } catch (Exception e) {
            System.out.println("Transaction Failure: " + e.getMessage());
            return false;
        }
        return true;
    }
}
