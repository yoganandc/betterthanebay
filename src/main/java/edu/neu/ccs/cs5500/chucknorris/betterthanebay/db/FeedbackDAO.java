package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.Query;
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
    public Feedback findById(Long itemId, String id) {
        Query query;

        if(id.equals(Feedback.BUYER)) {
            query = namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.BuyerFeedback.getForItem");
        }
        else {
            query = namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.SellerFeedback.getForItem");
        }

        return uniqueResult(query);
    }

    // Create new Feedback
    public Feedback create(Feedback feedback, String id) {
        if(id.equals( Feedback.BUYER)) {
            return persist(feedback.toBuyer());
        }
        else {
            return persist(feedback.toSeller());
        }
    }

    // Update Feedback with given information (have to check how it works)
    public Feedback update(Feedback feedback) {
        currentSession().clear();
        return persist(feedback);
    }

    public boolean deleteFeedback(Long id) {
        Feedback feedback = get(id);
        if(feedback == null) {
            return false;
        }
        else {
            currentSession().delete(feedback);
            return true;
        }
    }
}
