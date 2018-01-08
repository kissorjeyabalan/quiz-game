package no.octopod.quizgame.entities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class SubCategoryTest {
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
        SubCategory subCategory = new SubCategory();
        subCategory.setId(1L);
        assertNotNull(subCategory.getId());
        assertFalse(persist(subCategory));
    }

    @Test
    public void testSuccessfulPersistenceOnNullId() {
        SubCategory subCategory = new SubCategory();
        assertNull(subCategory.getId());
        persist(subCategory);
        assertNotNull(subCategory.getId());
    }

    @Test
    public void testPersistenceFailsOnSecondObjectWithSameId() {
        SubCategory subCategory1 = new SubCategory();
        SubCategory subCategory2 = new SubCategory();
        subCategory2.setId(1L);

        // subCategory1 should be persisted and subCategory2 should fail as duplicate id
        assertTrue(persist(subCategory1));
        assertFalse(persist(subCategory2));

    }

    @Test
    public void testPersistenceTwiceOnOneObject() {
        SubCategory subCategory = new SubCategory();
        subCategory.setName("World War 2");

        assertTrue(persist(subCategory));
        subCategory.setName("German Empire");

        assertTrue(persist(subCategory));
    }

    @Test
    public void testWriteRead() {
        String first = "first";
        String second = "second";

        SubCategory subCategory1 = new SubCategory();
        subCategory1.setName(first);

        SubCategory subCategory2 = new SubCategory();
        subCategory2.setName(second);

        persist(subCategory1);
        persist(subCategory2);

        // read from db, not cache
        em.close();
        em = emFactory.createEntityManager();

        //query for data
        Query query = em.createQuery("SELECT k FROM SubCategory k");
        List<SubCategory> subCategories = query.getResultList();

        assertThat(subCategories.size(), is(2));
        assertTrue(subCategories.stream().anyMatch(k -> k.getName().equals(first)));
        assertTrue(subCategories.stream().anyMatch(k -> k.getName().equals(second)));
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
