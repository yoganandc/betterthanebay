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
@Table(name = "buyer_feedback")
@NamedQueries(value = {
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.BuyerFeedback.getForItem",
                query = "SELECT f FROM BuyerFeedback f WHERE f.itemId = :item_id")
})
public class BuyerFeedback extends Feedback {

    public BuyerFeedback() {

    }

    public BuyerFeedback(SellerFeedback obj) {
        super(obj);
    }

    public BuyerFeedback(Long id, String message, Date created, Date updated, Integer rating, Long itemId) {
        super(id, message, created, updated, rating, itemId);
    }
}
