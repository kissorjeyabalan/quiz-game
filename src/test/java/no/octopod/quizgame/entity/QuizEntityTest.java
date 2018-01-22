package no.octopod.quizgame.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.*;
import java.util.List;
import java.util.function.Consumer;

import static org.junit.Assert.*;

public class QuizEntityTest extends EntityTestBase {

    @Test
    public void testQuiz() {
        Quiz quiz = new Quiz();
        quiz.setQuestion("What chemical element gives the blood of a lobster a bluish tint?");
        quiz.setFirstAnswer("Gold");
        quiz.setSecondAnswer("Copper");
        quiz.setThirdAnswer("Zinc");
        quiz.setFourthAnswer("Iridium");
        quiz.setCorrectAnswerIndex(1);

        assertNull(quiz.getId());
        boolean persisted = persistTransaction(quiz);

        assertFalse(persisted); // quiz does not have subcategory

        Category category = new Category();
        category.setName("Animals");
        SubCategory subCategory = createSubcategory(category, "Sea");
        quiz.setSubCategory(subCategory);

        quiz.setId(null);
        persisted = persistTransaction(category, subCategory, quiz);

        assertTrue(persisted);
        assertNotNull(quiz.getId());
    }

    @Test
    public void testQuizWithSubcategories() {
        Category animalCategory = new Category();
        animalCategory.setName("Animals");
        SubCategory birdSub = new SubCategory();
        birdSub.setName("Birds");
        Quiz quiz = new Quiz();
        quiz.setQuestion("What is a flock of crows called?");
        quiz.setFirstAnswer("Sloth");
        quiz.setSecondAnswer("Flock");
        quiz.setThirdAnswer("Murder");
        quiz.setFourthAnswer("Gang");
        quiz.setCorrectAnswerIndex(2);

        animalCategory.getSubCategories().add(birdSub);
        birdSub.setParent(animalCategory);
        quiz.setSubCategory(birdSub);

        assertNull(animalCategory.getId());
        assertNull(birdSub.getId());
        assertNull(quiz.getId());

        boolean persisted = runTransaction(e -> {
           e.persist(animalCategory);
           e.persist(birdSub);
           e.persist(quiz);
        });

        assertTrue(persisted);
        assertNotNull(animalCategory.getId());
        assertNotNull(birdSub.getId());
        assertNotNull(quiz.getId());
    }

    @Test
    public void testQueries() {
        Category jee = new Category();
        jee.setName("JEE");

        SubCategory jpa = createSubcategory(jee, "JPA");
        SubCategory ejb = createSubcategory(jee, "EJB");
        SubCategory jsf = createSubcategory(jee, "JSF");

        Quiz jpaQ1 = createQuiz(jpa, "What does JPA stand for?");
        Quiz jpaQ2 = createQuiz(jpa, "What is JPA?");
        Quiz ejbQ = createQuiz(ejb, "What does EJB stand for?");
        Quiz jsfQ = createQuiz(jsf, "What does JSF stand for?");

        persistTransaction(jee, jpa, ejb, jsf, jpaQ1, jpaQ2, ejbQ, jsfQ);

        TypedQuery<Quiz> queryJPAQuizzes = em.createQuery("select q from Quiz q WHERE q.subCategory.name='JPA'", Quiz.class);
        List<Quiz> quizzes = queryJPAQuizzes.getResultList();

        assertEquals(2, quizzes.size());
        assertTrue(quizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals(jpaQ1.getQuestion())));
        assertTrue(quizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals(jpaQ2.getQuestion())));

        TypedQuery<Quiz> queryJEEQuizzes = em.createQuery("select q from Quiz q WHERE q.subCategory.parent.name='JEE'", Quiz.class);
        quizzes = queryJEEQuizzes.getResultList();

        assertEquals(4, quizzes.size());
        assertTrue(quizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals(jpaQ1.getQuestion())));
        assertTrue(quizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals(jpaQ2.getQuestion())));
        assertTrue(quizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals(ejbQ.getQuestion())));
        assertTrue(quizzes.stream().anyMatch(quiz -> quiz.getQuestion().equals(jsfQ.getQuestion())));

    }

    private Quiz createQuiz(SubCategory parent, String question) {
        Quiz quiz = new Quiz();
        quiz.setQuestion(question);
        quiz.setFirstAnswer("Answer");
        quiz.setSecondAnswer("Answer");
        quiz.setThirdAnswer("Answer");
        quiz.setFourthAnswer("Answer");
        quiz.setCorrectAnswerIndex(1);
        quiz.setSubCategory(parent);
        return quiz;
    }

    private SubCategory createSubcategory(Category category, String name) {
        SubCategory sub = new SubCategory();
        sub.setName(name);

        sub.setParent(category);
        category.getSubCategories().add(sub);

        return sub;
    }
}
