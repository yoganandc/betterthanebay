package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.SessionFactory;

import java.util.List;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Category;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by yoganandc on 7/22/16.
 */
public class CategoryDAO extends AbstractDAO<Category> {

    public CategoryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Category> getAllCategories() {
        return list(namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Category.findAll"));
    }
}
