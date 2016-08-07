package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by alesyatrubchik on 6/29/16.
 */
public class ItemDAO extends AbstractDAO<Item> {

    public ItemDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

     public Item findById(Long id) {
        return get(id);
     }

    // Create new Item
    public Item create(Item item) {
        return persist(item);
    }

    // Update bid with given information (have to check how it works)
    public Item update(Item item) {
        currentSession().clear();
        return persist(item);
    }

    // Deletes item with given id
    public boolean deleteItem(Long id) {
        Item item = get(id);
        if(item == null) {
            return false;
        }
        else {
            currentSession().delete(item);
            return true;
        }
    }

    // Returns all items for given userId
    public List<Item> getAllItems(Long userId) {
        Query query =
                super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsForUser")
                        .setParameter("user_id", userId);
        List<Item> list = list(query);
        return list;
    }

    // Returns all Active Items aka End Date is in the Future by given UserId
    public List<Item> getActiveItems(Long userId) {
        Query query =
                super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getActiveItemsForUser")
                        .setParameter("user_id", userId);
        List<Item> list = list(query);
        return list;
    }

    public List<Item> searchWithCategory(String queryString, BigDecimal startPrice, BigDecimal endPrice,
                                         Long categoryId, Integer start, Integer end) {

        queryString = "%" + queryString + "%";
        Query query = super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsWithCategory")
                .setParameter("query", queryString)
                .setParameter("start_price", startPrice)
                .setParameter("end_price", endPrice)
                .setParameter("category_id", categoryId)
                .setFirstResult(start)
                .setMaxResults(end);
        return list(query);
    }

    public List<Item> searchWithoutCategory(String queryString, BigDecimal startPrice, BigDecimal endPrice,
                                            Integer start, Integer end) {

        queryString = "%" + queryString + "%";
        Query query = super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsWithoutCategory")
                .setParameter("query", queryString)
                .setParameter("start_price", startPrice)
                .setParameter("end_price", endPrice)
                .setFirstResult(start)
                .setMaxResults(end);
        return list(query);
    }

}
