package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import java.util.Optional;

import org.hibernate.SessionFactory;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by alesyatrubchik on 6/29/16.
 */
public class BidDAO extends AbstractDAO<Bid> {

  public BidDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  // Find Bid by ID
  public Optional<Bid> findById(Long id) {
    return Optional.ofNullable(get(id));
  }

  // Create new Bid
  public Bid create(Bid bid) {
    return persist(bid);
  }

  // Update bid with given information (have to check how it works)
  public Bid update(Bid bid) {
    return persist(bid);
  }
}
