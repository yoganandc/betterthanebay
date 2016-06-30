package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by alesyatrubchik on 6/29/16.
 */
public class ItemDAO extends AbstractDAO<Item> {

    public ItemDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
