package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import java.util.Optional;

import org.hibernate.SessionFactory;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by yoganandc on 6/28/16.
 */
public class UserDAO extends AbstractDAO<User> {

  public UserDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  // Find User by ID
  public Optional<User> findById(Long id) {
    return Optional.ofNullable(get(id));
  }

  // Create new User
  public User create(User user) {
    return persist(user);
  }

  // Update User with given information (have to check how it works)
  public User update(User user) {
    return persist(user);
  }

}
