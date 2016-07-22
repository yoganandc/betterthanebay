package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by alesyatrubchik on 6/29/16.
 */
public class ItemDAO extends AbstractDAO<Item> {

    public ItemDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    // Find Item by ID
    public Item findById(Long id) {
        return get(id);
    }

    // Create new Item
    public Item create(Item item) {
        return persist(item);
    }

    // Update bid with given information (have to check how it works)
    public Item update(Item item) {
        return persist(item);
    }

    public List<Item> getItems(Long userId) {
        Query query = super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsForUser")
                .setParameter("user_id", userId);
        List<Item> list = list(query);
        return list;
    }

}
