package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import java.util.Optional;

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

  // Find Item by ID
  public Optional<Item> findById(Long id) {
    return Optional.ofNullable(get(id));
  }

  // Create new Item
  public Item create(Item item) {
    return persist(item);
  }

  // Update bid with given information (have to check how it works)
  public Item update(Item item) {
    return persist(item);
  }
}
