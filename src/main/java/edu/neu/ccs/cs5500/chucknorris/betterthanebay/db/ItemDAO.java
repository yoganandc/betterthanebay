package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * Created by alesyatrubchik on 6/29/16.
 */
public class ItemDAO extends AbstractDAO<Item> {

    public ItemDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Item> getById(Long id) {
        Optional<Item> ret = Optional.ofNullable(get(id));
        return ret;
    }
}
