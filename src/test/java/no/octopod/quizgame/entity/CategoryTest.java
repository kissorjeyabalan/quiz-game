package no.octopod.quizgame.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class CategoryTest {

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
        Category category = new Category();
        category.setId(1L);
        assertNotNull(category.getId());
        assertFalse(persist(category));
    }

    @Test
    public void testSuccessfulPersistenceOnNullId() {
        Category category = new Category();
        assertNull(category.getId());
        persist(category);
        assertNotNull(category.getId());
    }

    @Test
    public void testPersistenceFailsOnSecondObjectWithSameId() {
        Category category1 = new Category();
        Category category2 = new Category();
        category2.setId(1L);

        // category1 should be persisted and category2 should fail as duplicate id
        assertTrue(persist(category1));
        assertFalse(persist(category2));

    }

    @Test
    public void testPersistenceTwiceOnOneObject() {
        Category category = new Category();
        category.setName("History");

        assertTrue(persist(category));
        category.setName("Music");

        assertTrue(persist(category));
    }

    @Test
    public void testWriteRead() {
        String first = "first";
        String second = "second";

        Category category1 = new Category();
        category1.setName(first);

        Category category2 = new Category();
        category2.setName(second);

        persist(category1);
        persist(category2);

        // read from db, not cache
        em.close();
        em = emFactory.createEntityManager();

        //query for data
        Query query = em.createQuery("SELECT k FROM Category k");
        List<Category> categories = query.getResultList();

        assertThat(categories.size(), is(2));
        assertTrue(categories.stream().anyMatch(k -> k.getName().equals(first)));
        assertTrue(categories.stream().anyMatch(k -> k.getName().equals(second)));
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
