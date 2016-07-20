package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Address;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Payment;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "/users", description = "Users")
public class UserResource {


  /*
   * TO DO user authentication for post, put, delete
   */

    private final UserDAO dao;
    private final ItemDAO itemDAO;
    private final BidDAO bidDAO;
    private final FeedbackDAO feedbackDAO;

    public UserResource(UserDAO dao, ItemDAO itemDAO, BidDAO bidDAO, FeedbackDAO feedbackDAO) {
        this.dao = dao;
        this.itemDAO = itemDAO;
        this.bidDAO = bidDAO;
        this.feedbackDAO = feedbackDAO;
    }

    @GET
    @Path("/{userId}")
    @UnitOfWork
    @ApiOperation(
            value = "Find user with given id",
            notes = "If {userId} exists, returns the corresponding user object",
            response = User.class)
    public Response getUser(@PathParam("userId") LongParam userId, @Auth User loggedInUser) {

        final User user = this.dao.findById(userId.get());
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(user).build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(
            value = "Find users whose account username contains the given username",
            notes = "Returns a list of users",
            response = User.class,
            responseContainer = "List")
    public Response searchByUsername(@QueryParam("username") String username,
                                     @QueryParam("start") IntParam start, @QueryParam("size") IntParam size,
                                     @Auth User loggedInUser) {

        if (username == null) {
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

        List<User> list = this.dao.searchByUsername(username, startVal, sizeVal);

        if (list == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else if (list.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(list).build();
    }


    @POST
    @UnitOfWork
    @ApiOperation(
            value = "Creates a new user account",
            notes = "Adds the given user to the database",
            response = User.class)
    public Response addUser(@Valid User user) {

        Response.ResponseBuilder response;

        // NULL OUT ALL IDs
//        user.setId(null);
//        for(Address address : user.getAddresses()) {
//            address.setId(null);
//        }
//        for(Payment payment : user.getPayments()) {
//            payment.setId(null);
//            payment.getAddress().setId(null);
//        }

        User createdUser = dao.create(user);

        if (createdUser == null) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR);

        } else { // user successfully created
            response = Response.created(URI.create("/users/" + createdUser.getId())).entity(createdUser);
        }

        return response.build();
    }

    @PUT
    @Path("/{userId}")
    @UnitOfWork
    @ApiOperation(
            value = "Updates the user account",
            notes = "Updates the given user's account information",
            response = User.class)
    public Response updateUser(@PathParam("userId") LongParam userId, @Valid User user,
                               @Auth User loggedInUser) {

        // FORBIDDEN TO UPDATE IF USER LOGGED IN IS NOT THE SAME
        if(userId.get() != loggedInUser.getId()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // BAD REQUEST IF ENTITY OBJECT'S ID DOES NOT MATCH PATH ID
        if(userId.get() != user.getId()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // NOT FOUND
        if (this.dao.findById(userId.get()) == null) {
            Response.status(Response.Status.NOT_FOUND).build();
        }

        User updatedUser = dao.update(user);

        return Response.ok(updatedUser).build();
    }

    @DELETE
    @Path("/{userId}")
    @UnitOfWork
    public Response deleteUser(@PathParam("userId") LongParam userId, @Auth User loggedInUser) {

        // FORBIDDEN TO DELETE IF USER LOGGED IN IS NOT THE SAME
        if(userId.get() != loggedInUser.getId()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (this.dao.findById(userId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // userId doesn't exist
        }

        boolean success = this.dao.deleteUser(userId.get());
        if (success) {
            return Response.status(Response.Status.NO_CONTENT).build(); // user account successfully
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // failure
        }
    }

    @GET
    @Path("/{userId}/items")
    @UnitOfWork
    public Response getItemsForUser(@PathParam("userId") LongParam userId, @Auth User loggedInUser) {

        final List<Item> items = this.itemDAO.getItems(userId.get());

        if (items == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if (items.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(items).build();
    }

    @GET
    @Path("/{userId}/bids")
    @UnitOfWork
    public Response getBidsForUser(@PathParam("userId") LongParam userId, @Auth User loggedInUser) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    // seller feedback by user
    @GET
    @Path("/{userId}/feedback/{feedbackId}")
    @UnitOfWork
    public Response getSellerFeedback(@PathParam("userId") LongParam userId,
                                      @PathParam("feedbackId") NonEmptyStringParam feedbackId, @Auth User loggedInUser) {

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
