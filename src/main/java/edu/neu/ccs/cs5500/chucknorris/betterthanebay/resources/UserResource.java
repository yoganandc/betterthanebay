package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import java.util.List;

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

//    @GET
//    @Path("/{userId}")
//    public User getUser(@PathParam("userId") long userId) {
//
//        return dao.getUser(userId);
//    }

    // query by user name or get all users if query parameter is blank
    @GET
    public List<User> getUser(@QueryParam("username") String username) {
        if ((username != null) && !username.isEmpty()) {
            // return by user
            return null; //dao.getByUserName(username);
        }
        // return all users
        return null; //dao.getAllUsers();
    }


    @POST
    public Response addUser(User user) {

        Response.ResponseBuilder response;

        // FORBIDDEN if already logged in as another user

        if ((user.getUsername() == null) || (user.getPassword() != null)
                || (user.getDetails() != null)) {
            response = Response.status(Response.Status.BAD_REQUEST);

        } else {
            User createdUser = null; //dao.createUser(user); /// if createUser returns user
            if (createdUser == null) {
                response = Response.status(Response.Status.BAD_REQUEST);
                //
            } else {
                response = Response.status(Response.Status.CREATED);
                // add user data
            }

        }

        return response.build();
    }

    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") long userId, User user) {
        Response.ResponseBuilder response;
        if (user.getId().equals(userId)) { // validate userId ??
            response = Response.status(Response.Status.OK);
            // update data
        } else { // userId does not match user
            response = Response.status(Response.Status.UNAUTHORIZED);
        }

        return response.build();
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") long userId) {

        // authenticate user --> UNAUTHORIZED

        Response.ResponseBuilder response;
        User user = null; // dao.getUser(userId);
        if (user == null) {
            response = Response.status(Response.Status.BAD_REQUEST); // invalid user id
        }

        boolean success = false; // dao.deleteUser(userId);
        if (success) {
            response = Response.status(Response.Status.OK); // user account successfully deleted
        } else {
            response = Response.status(Response.Status.BAD_REQUEST); // failure
        }

        return response.build();
    }
}
