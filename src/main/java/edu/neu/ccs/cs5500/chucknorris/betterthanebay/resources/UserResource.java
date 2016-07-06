package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {


    /* TO DO
     * user authentication for post, put, delete
     */

    private final UserDAO dao;

    public UserResource(UserDAO dao) {
        this.dao = dao;
    }

    @GET
    @Path("/{userId}")
    @UnitOfWork
    public User getUser(@PathParam("userId") LongParam userId) {
        final User user = dao.getById(userId.get());
        if (user == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return user;
    }

    /* query by username or get all users if username parameter is blank */
    @GET
    public List<User> getUsers(@QueryParam("username") Optional<String> username) {
        if ((username != null) && username.isPresent()) {
            /* return by user */
            return null; //dao.getByUsername(username.get());
        }
        /* return all users */
        return null; //dao.getAllUsers();
    }


    @POST
    public Response addUser(@Valid User user) {

        /* @Valid should test:
         * user.getUsername() != null
         * user.getPassword() != null
         * user.getDetails != null */


        Response.ResponseBuilder response;

        /* TO DO
         * FORBIDDEN if already logged in as another user ?
         */

        Long userId = null; //dao.createUser(user);

        if (userId == null) {
            response = Response.status(Response.Status.BAD_REQUEST);

        } else {  // if createUser returns valid id
            response = Response.created(URI.create("/users/" + userId));
        }

        return response.build();
    }

    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") LongParam userId, User user) {
        Response.ResponseBuilder response;
        if (user.getId().equals(userId)) { // validate userId ??
            boolean success = false; // dao.updateUser(userId, user);
            if (success) {
                response = Response.ok(getUser(userId));
            } else {
                response = Response.notModified();
            }

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