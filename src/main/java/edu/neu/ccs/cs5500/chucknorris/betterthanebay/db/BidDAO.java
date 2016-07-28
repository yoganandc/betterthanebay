package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import io.dropwizard.hibernate.AbstractDAO;

public class BidDAO extends AbstractDAO<Bid> {

  public BidDAO(SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  // Find Bid by ID
  public Bid findById(Long id) {
    return get(id);
  }

  // Create new Bid
  public Bid create(Bid bid) {
    return persist(bid);
  }

  // Update bid with given information (have to check how it works)
  public Bid update(Bid bid) {
    currentSession().clear();
    return persist(bid);
  }

  public boolean deleteBid(Long id) {
    Bid bid = get(id);
    if (bid == null) {
      return false;
    } else {
      currentSession().delete(bid);
      return true;
    }
  }

  public Bid getCurrentWinningBid(Long itemId) {
    Query query = super.namedQuery(
        "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid.getCurrentWinningBid")
            .setParameter("item_id", itemId)
            .setMaxResults(1);
    List<Bid> bids = super.list(query);
      if(bids.isEmpty()) {
          return null;
      }
      else {
          return bids.get(0);
      }
  }

    public List<Bid> getBidsForUser(Long userId) {
        Query query = super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid.getBidsForUser")
                .setParameter("user_id", userId);
        return list(query);
    }

    public List<Bid> getBidsForItem(Long itemId) {
        Query query = super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid.getBidsForItem")
                .setParameter("item_id", itemId);
        return list(query);
    }
}
