package ejb;

import no.octopod.quizgame.entity.Category;
import no.octopod.quizgame.entity.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CategoryBean {

    @PersistenceContext
    private EntityManager em;

    public Long createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        em.persist(category);

        return category.getId();
    }

    public Long createSubcategory(long parentId, String name) {
        Category parent = em.find(Category.class, parentId);
        if (parent == null) {
            throw new IllegalArgumentException("Parent with ID " + parentId + " does not exist.");
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setParent(parent);

        parent.getSubCategories().add(subCategory);

        em.persist(subCategory);
        em.merge(parent);

        return subCategory.getId();
    }

    public List<Category> getAllCategories(boolean withSub) {
        TypedQuery<Category> query = em.createQuery("select c from Category c", Category.class);
        List<Category> categories = query.getResultList();

        if (withSub) {
            for (Category cat : categories) {
                cat.getSubCategories().size();
            }
        }

        return categories;
    }

    public Category getCategory(long id, boolean withSub) {
        Category category = em.find(Category.class, id);
        if (category != null && withSub) {
            category.getSubCategories().size();
        }

        return category;
    }

    public SubCategory getSubCategory(long id) {
        return em.find(SubCategory.class, id);
    }
}
