package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.SessionFactory;

import java.util.List;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.State;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by yoganandc on 7/22/16.
 */
public class StateDAO extends AbstractDAO<State> {

    public StateDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<State> getAllCategories() {
        return list(namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.State.findAll"));
    }
}
