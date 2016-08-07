package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by yoganandc on 7/22/16.
 */
@Entity
@Table(name = "seller_feedback")
@NamedQueries(value = {
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.SellerFeedback.getForItem",
                query = "SELECT f FROM SellerFeedback f WHERE f.itemId = :item_id"),
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.SellerFeedback.getForUser",
                    query = "SELECT f FROM SellerFeedback f WHERE f.userId = :user_id")
})
public class SellerFeedback extends Feedback {

    public SellerFeedback() {

    }

    public SellerFeedback(SellerFeedback obj) {
        super(obj);
    }

    public SellerFeedback(Long id, String message, Date created, Date updated, Integer rating, Long itemId, Long userId) {
        super(id, message, created, updated, rating, itemId, userId);
    }
}
