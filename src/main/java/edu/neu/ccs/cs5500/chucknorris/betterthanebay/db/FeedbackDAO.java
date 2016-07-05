package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Feedback;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Optional;

/**
 * Created by alesyatrubchik on 6/29/16.
 */
public class FeedbackDAO extends AbstractDAO<Feedback> {

    public FeedbackDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public Optional<Feedback> getById(Long id) {
        Optional<Feedback> ret = Optional.ofNullable(get(id));
        return ret;
    }
}
