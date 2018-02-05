package no.octopod.quizgame.ejb;

import no.octopod.quizgame.entity.Category;
import no.octopod.quizgame.entity.SubCategory;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import javax.ejb.EJB;
import java.util.List;

@RunWith(Arquillian.class)
public class CategoryEjbTest {

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "no.octopod.quizgame")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private ResetState reset;

    @EJB
    private CategoryBean cat;


    @Before
    public void tearDown() {
        reset.resetEjb();
    }

    @Test
    public void testNoCategory() {
        List<Category> catList = cat.getAllCategories(false);
        assertEquals(0, catList.size());
    }

    @Test
    public void testCreateCategory() {
        Long id = cat.createCategory("testCat");
        assertNotNull(id);
    }

    @Test
    public void testGetCategory() {
        String name = "testCat";
        Long id = cat.createCategory(name);
        Category dbCat = cat.getCategory(id, false);
        assertEquals(name, dbCat.getName());
    }

    @Test
    public void testCreateSubCategory() {
        String parentName = "parentCat";
        String subName = "subCat";
        Long subId = cat.createSubCategory(cat.createCategory(parentName), subName);
        SubCategory subCat = cat.getSubCategory(subId);

        assertEquals(subName, subCat.getName());
        assertEquals(parentName, subCat.getParent().getName());
    }

    @Test
    public void testGetAllCategories() {
        Long catOneId = cat.createCategory("cat1");
        Long catTwoId = cat.createCategory("cat2");
        Long catThreeId = cat.createCategory("cat3");

        cat.createSubCategory(catOneId, "sub1");
        cat.createSubCategory(catTwoId, "sub2");
        cat.createSubCategory(catThreeId, "sub3");

        List<Category> categories = cat.getAllCategories(false);
        assertEquals(3, categories.size());

        try {
            categories.get(0).getSubCategories().size();
            fail();
        } catch (Exception e) {
            // should throw exception
        }

        categories = cat.getAllCategories(true);
        assertEquals(1, categories.get(0).getSubCategories().size());
    }

    @Test
    public void testCreateTwice() {
        String name = "testCat";
        cat.createCategory(name);

        try {
            cat.createCategory(name);
            fail();
        } catch (Exception e) {
            // should throw exception
        }
    }
}
