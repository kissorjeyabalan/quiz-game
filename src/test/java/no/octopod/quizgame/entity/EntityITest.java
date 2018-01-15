package no.octopod.quizgame.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;
import java.util.function.Consumer;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class EntityITest {

    private EntityManagerFactory factory;

    @Before
    public void setUp() {
        factory = Persistence.createEntityManagerFactory("database");
    }

    @After
    public void tearDown() {
        factory.close();
    }

    @Test
    public void testQuizWithSubcategory() {
        Category category = new Category();
        category.setName("Software Engineering");

        SubCategory subCategory = new SubCategory();
        subCategory.setName("JPA");

        Quiz quiz = new Quiz();
        quiz.setQuestion("What does JPA abbreviate?");

        quiz.setSubCategory(subCategory);
        subCategory.setParent(category);
        category.getSubCategories().add(subCategory);

        boolean persisted = runTransaction(entityManager -> {
            entityManager.persist(category);
            entityManager.persist(subCategory);
            entityManager.persist(quiz);
        });

        assertTrue(persisted);
    }

    @Test
    public void testQueries() {
        Category category = new Category();
        category.setName("JEE");

        SubCategory jpa = createSubcategory(category, "JPA");
        SubCategory ejb = createSubcategory(category, "EJB");
        SubCategory jsf = createSubcategory(category, "JSF");

        Quiz a = createQuiz(jpa, "A");
        Quiz b = createQuiz(jpa, "B");
        Quiz c = createQuiz(ejb, "C");
        Quiz d = createQuiz(jsf, "D");

        boolean persisted = runTransaction(entityManager -> {
            entityManager.persist(category);
            entityManager.persist(jpa);
            entityManager.persist(ejb);
            entityManager.persist(jsf);
            entityManager.persist(a);
            entityManager.persist(b);
            entityManager.persist(c);
            entityManager.persist(d);
        });

        assertTrue(persisted);

        EntityManager em = factory.createEntityManager();

        TypedQuery<Quiz> queryAllJPA = em.createQuery(
                "select q from Quiz q where q.subCategory.name = 'JPA'", Quiz.class);

        List<Quiz> jpaQuizzes = queryAllJPA.getResultList();
        assertEquals(2, jpaQuizzes.size());
        assertTrue(jpaQuizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals("A")));
        assertTrue(jpaQuizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals("B")));

        TypedQuery<Quiz> queryAllJEE = em.createQuery(
                "select q from Quiz q where q.subCategory.parent.name = 'JEE'", Quiz.class);

        List<Quiz> jeeQuizzes = queryAllJEE.getResultList();
        assertEquals(4, jeeQuizzes.size());
        assertTrue(jeeQuizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals("A")));
        assertTrue(jeeQuizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals("B")));
        assertTrue(jeeQuizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals("C")));
        assertTrue(jeeQuizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals("D")));

        em.close();

    }

    private Quiz createQuiz(SubCategory parent, String question) {
        Quiz quiz = new Quiz();
        quiz.setSubCategory(parent);
        quiz.setQuestion(question);
        return quiz;
    }

    private SubCategory createSubcategory(Category parent, String subCategoryName) {
        SubCategory subCategory = new SubCategory();
        subCategory.setName(subCategoryName);
        subCategory.setParent(parent);
        return subCategory;
    }

    private boolean runTransaction(Consumer<EntityManager> lambda) {
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            lambda.accept(em);
            tx.commit();
        } catch (Exception e) {
            System.out.println("Transaction failure: " + e.getMessage());
            tx.rollback();
            return false;
        } finally {
            em.close();
        }
        return true;
    }
}
