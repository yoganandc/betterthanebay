package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import java.util.Optional;

import org.hibernate.SessionFactory;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Feedback;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by alesyatrubchik on 6/29/16.
 */
public class FeedbackDAO extends AbstractDAO<Feedback> {

  public FeedbackDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  // Find Feedback by ID
  public Feedback findById(Long id) {
    return get(id);
  }

  // Create new Feedback
  public Feedback create(Feedback feedback) {
    return persist(feedback);
  }

  // Update Feedback with given information (have to check how it works)
  public Feedback update(Feedback feedback) {
    return persist(feedback);
  }
}
