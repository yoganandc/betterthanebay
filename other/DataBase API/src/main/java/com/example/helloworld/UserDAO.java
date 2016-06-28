package com.example.helloworld;

import java.util.List;

import org.hibernate.SessionFactory;

import com.google.common.base.Optional;

import io.dropwizard.hibernate.AbstractDAO;

public class UserDAO extends AbstractDAO<User> {
  /**
   * Constructor.
   *
   * @param sessionFactory Hibernate session factory.
   */
  public UserDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * Method returns all users stored in the database.
   *
   * @return list of all users stored in the database
   */
  public List<User> findAll() {
    return list(namedQuery("findAll"));
  }

  /**
   * Looks for users whose first or last name contains the passed parameter as a substring.
   *
   * @param name query parameter
   * @return list of users whose first or last name contains the passed parameter as a substring.
   */
  public List<User> findByName(String name) {
    StringBuilder builder = new StringBuilder("%");
    builder.append(name).append("%");
    return list(namedQuery("findByName").setParameter("name", builder.toString()));
  }

  /**
   * Method looks for an user by her id.
   *
   * @param id the id of an employee we are looking for.
   * @return Optional containing the found employee or an empty Optional otherwise.
   */
  public Optional<User> findById(long id) {
    return Optional.fromNullable(get(id));
  }
}
