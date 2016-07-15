package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;

import java.net.URI;
import java.util.List;


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
    public Response getUser(@PathParam("userId") LongParam userId, @Auth User loggedInUser) {

        final User user = dao.findById(userId.get());
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(user).build();
    }

    @GET
    public Response searchByUsername(@QueryParam("username") NonEmptyStringParam username, @QueryParam("start") IntParam start,
                               @QueryParam("size") IntParam size, @Auth User loggedInUser) {
        if(username == null || !username.get().isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        int startVal;
        if (start == null) {
            startVal = 0;
        } else {
            startVal = start.get();
        }

        int sizeVal;
        if (size == null) {
            sizeVal = 20;
        } else {
            sizeVal = size.get();
        }

        /* return by user */
        List<User> list = null; //dao.findByUsername(username.get(), startVal, sizeVal);

        if (list == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else if (list.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(list).build();
    }


    @POST
    public Response addUser(@Valid User user) {

        Response.ResponseBuilder response;

        /* TO DO
         * FORBIDDEN if already logged in as another user ?
         */

        User createdUser = null; //dao.createUser(user);

        if (createdUser == null) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);

        } else { // user successfully created
            response = Response.created(URI.create("/users/" + createdUser.getId())).entity(createdUser);
        }

        return response.build();
    }

    @PUT
    @Path("/{userId}")
    public Response updateUser(@PathParam("userId") LongParam userId, @Valid User user, @Auth User loggedInUser) {

        if (userId.get() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if (dao.findById(userId.get()) == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }

        User updatedUser = null; // dao.updateUser(userId, user);

        return Response.ok(updatedUser).build();
    }

    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") LongParam userId, @Auth User loggedInUser) {

        if (dao.findById(userId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // userId doesn't exist
        }

        boolean success = false; // dao.deleteUser(userId);
        if (success) {
            return Response.status(Response.Status.NO_CONTENT).build(); // user account successfully deleted
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // failure
        }
    }

    // seller feedback by user
    @GET
    @Path("/{userId}/feedback/{feedbackId}")
    public Response getSellerFeedback(@PathParam("userId") LongParam userId,
                                      @PathParam("feedbackId") NonEmptyStringParam feedbackId, @Auth User loggedInUser) {

        // if valid user
        return null; //dao.getSellerFeedback(userId);
    }

    @GET
    @Path("/{userId}/bids")
    public Response getBidsForUser(@PathParam("userId") LongParam userId, @Auth User loggedInUser) {
        return null; //return dao.getActiveBids(userId);
    }

    @GET
    @Path("/{userId}/items")
    public Response getItemsForUser(@PathParam("userId") LongParam userId, @Auth User loggedInUser) {
        return null; //return dao.getItems(userId);
    }
}