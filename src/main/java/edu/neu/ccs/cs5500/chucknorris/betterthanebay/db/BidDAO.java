package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

/**
 * Created by alesyatrubchik on 6/29/16.
 */
public class BidDAO extends AbstractDAO<Bid> {

    public BidDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
