package no.octopod.quizgame.entity;


import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CategoryEntityTest extends EntityTestBase {

    @Test
    public void testTooLongName() {
        Category category = new Category();
        category.setName("this" + new String(new char[256]));

        boolean persisted = persistTransaction(category);
        assertFalse(persisted);

        category = new Category();
        category.setName("foo");

        persisted = persistTransaction(category);
        assertTrue(persisted);
    }

    @Test
    public void testUniqueName() {
        Category category = new Category();
        category.setName("Animals");

        boolean persisted = persistTransaction(category);
        assertTrue(persisted);

        Category category2 = new Category();
        category2.setName("Animals");

        persisted = persistTransaction(category2);
        assertFalse(persisted);
    }
}
