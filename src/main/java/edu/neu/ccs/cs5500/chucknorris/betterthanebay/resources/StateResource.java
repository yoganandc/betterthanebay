package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.State;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.StateDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * Created by yoganandc on 7/22/16.
 */
@Path("/states")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StateResource {

    private final StateDAO stateDAO;

    public StateResource(StateDAO stateDAO) {
        this.stateDAO = stateDAO;
    }

    @GET
    @UnitOfWork
    public List<State> getAllStates() {
        return stateDAO.getAllCategories();
    }
}
