package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.util.Optional;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.api.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

/**
 * Created by yoganandc on 6/28/16.
 */
@Path("/users")
public class UserResource {

    private final UserDAO dao;

    public UserResource(UserDAO dao) {
        this.dao = dao;
    }

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @UnitOfWork
    public User getUser(@PathParam("userId") LongParam userId) {
        User ret = dao.getById(userId.get()).orElseThrow(() -> new NotFoundException("No such user"));
        return ret;
    }
}
