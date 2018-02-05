package no.octopod.quizgame.ejb;

import no.octopod.quizgame.entity.Category;
import no.octopod.quizgame.entity.Quiz;
import no.octopod.quizgame.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ResetState {
    @PersistenceContext
    private EntityManager em;

    public void resetEjb() {
        delete(Quiz.class);
        delete(SubCategory.class);
        delete(Category.class);
    }

    private void delete(Class<?> clazz) {
        if (clazz == null || clazz.getAnnotation(Entity.class) == null) {
            throw new IllegalArgumentException("Class is not of type Entity.");
        }

        String className = clazz.getSimpleName();
        Query query = em.createQuery("delete from " + className);
        query.executeUpdate();
    }
}
