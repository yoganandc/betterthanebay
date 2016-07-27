package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.List;

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
            return uniqueResult(query);
        }
        else if(id.equals(Feedback.SELLER)){
            query = namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.SellerFeedback.getForItem");
            return uniqueResult(query);
        }

        return null;
    }

    // Create new Feedback
    public Feedback create(Feedback feedback, String id) {
        return persistFeedback(feedback, id);
    }

    // Update Feedback with given information (have to check how it works)
    public Feedback update(Feedback feedback, String id) {
        currentSession().clear();
        return persistFeedback(feedback, id);
    }

    public boolean deleteFeedback(Long itemId, String id) {
        Feedback feedback = findById(itemId, id);
        if(feedback == null) {
            return false;
        }
        else if(id.equals(Feedback.SELLER)){
            currentSession().delete(feedback.toSeller());
            return true;
        }
        else {
            currentSession().delete(feedback.toBuyer());
            return true;
        }
    }

    public List<Feedback> getFeedbackForUser(Long userId, String id) {
        if(id.equals(Feedback.SELLER)) {
            return list(namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.SellerFeedback.getForUser")
            .setParameter("user_id", userId));
        }
        else if(id.equals(Feedback.BUYER)) {
            return list(namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.BuerFeedback.getForUser")
            .setParameter("user_id", userId));
        }
        else {
            return null;
        }
    }

    private Feedback persistFeedback(Feedback feedback, String id) {
        if (id.equals(Feedback.BUYER)) {
            return persist(feedback.toBuyer());
        } else if (id.equals(Feedback.SELLER)) {
            return persist(feedback.toSeller());
        } else {
            return null;
        }
    }
}
