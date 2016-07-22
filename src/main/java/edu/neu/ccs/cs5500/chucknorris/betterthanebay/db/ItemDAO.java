package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

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

  // // (DOESN'T WORK) Find Item by ID
  // public Item findById(Long id) {
  // return get(id);
  // }

  // Create new Item
  public Item create(Item item) {
    return persist(item);
  }

  // Update bid with given information (have to check how it works)
  public Item update(Item item) {
    return persist(item);
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
        super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getActiveItems")
            .setParameter("user_id", userId);
    List<Item> list = list(query);
    return list;
  }

  // Finds Item by its Id
  public Item findById(Long id) {
    Query query =
        super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.findById")
            .setParameter("id", id);
    Item item = super.uniqueResult(query);
    return item;
  }

  // Deletes item with given id
  public boolean deleteItem(Long id) {
    Query query =
        super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.deleteItem")
            .setParameter("id", id);
    query.executeUpdate();
    return true;
  }

}
