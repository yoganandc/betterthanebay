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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by yoganandc on 7/22/16.
 */
@Path("/states")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "state")
public class StateResource {

    private final StateDAO stateDAO;

    public StateResource(StateDAO stateDAO) {
        this.stateDAO = stateDAO;
    }

    @GET
    @UnitOfWork
    @ApiOperation(
            value = "Returns a list of all US states",
            notes = "Returns a list of all US states",
            response = State.class,
            responseContainer = "List")
    public List<State> getAllStates() {
        return stateDAO.getAllCategories();
    }
}
