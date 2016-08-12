package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.net.URI;
<<<<<<< HEAD
=======
import java.net.URL;
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
<<<<<<< HEAD
import javax.ws.rs.WebApplicationException;
=======
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.PasswordUtil;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.*;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
<<<<<<< HEAD

import static javax.ws.rs.core.Response.Status;
=======
import io.swagger.annotations.Authorization;
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc


@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "user")
public class UserResource {

    private final UserDAO dao;
    private final ItemDAO itemDAO;
    private final BidDAO bidDAO;
    private final FeedbackDAO feedbackDAO;
    private final PasswordUtil util;

    public UserResource(UserDAO dao, ItemDAO itemDAO, BidDAO bidDAO, FeedbackDAO feedbackDAO, PasswordUtil util) {
        this.dao = dao;
        this.itemDAO = itemDAO;
        this.bidDAO = bidDAO;
        this.feedbackDAO = feedbackDAO;
        this.util = util;
    }

    @GET
    @Path("/{userId}")
    @UnitOfWork
    @ApiOperation(value = "Find user with given id",
            notes = "If {userId} exists, returns the corresponding user object", response = User.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "User ID doesn't exist")})
<<<<<<< HEAD
    public User getUser(
=======
    public Response getUser(
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
            @ApiParam(value = "ID of a user", required = true) @PathParam("userId") LongParam userId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        final User user = this.dao.findById(userId.get());
        if (user == null) {
<<<<<<< HEAD
            throw new WebApplicationException("User does not exist", Status.NOT_FOUND);
        }

        if (user.getId().equals(loggedInUser.getId())) {
            return user;
=======
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage("user not found")).build();
        }

        if (user.getId().equals(loggedInUser.getId())) {
            return Response.ok(user).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
        } else {
            User userCopy = new User(user);
            userCopy.setPassword(null);
            userCopy.getPayments().clear();
            userCopy.getAddresses().clear();
            userCopy.setDetails(null);
<<<<<<< HEAD
            return userCopy;
=======
            return Response.ok(userCopy).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
        }
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Find users whose account username contains the given username",
            notes = "Returns a list of users", response = User.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No matching usernames found"),
            @ApiResponse(code = 400, message = "No username entered"),
            @ApiResponse(code = 401, message = "User must be logged in")})
<<<<<<< HEAD
    public List<User> searchByUsername(
=======
    public Response searchByUsername(
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
            @ApiParam(value = "part of a username",
                    required = true) @QueryParam("username") NonEmptyStringParam username,
            @ApiParam(value = "results offset", required = false) @QueryParam("start") IntParam start,
            @ApiParam(value = "results list size", required = false) @QueryParam("size") IntParam size,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        if (username == null || !username.get().isPresent()) {
<<<<<<< HEAD
            throw new WebApplicationException("No username entered", Status.BAD_REQUEST);
=======
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessage("No username entered")).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
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

        List<User> list = this.dao.searchByUsername(username.get().get(), startVal, sizeVal);

        if (list.isEmpty()) {
<<<<<<< HEAD
            throw new WebApplicationException("No users found", Status.NO_CONTENT);
=======
            return Response.status(Response.Status.NO_CONTENT).entity(new ErrorMessage("No results for username found")).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
        }

        List<User> listCopy = new ArrayList<>();

        for(User user : list) {
            User userCopy = new User(user);
            if(!userCopy.getId().equals(loggedInUser.getId())) {
                userCopy.setPassword(null);
                userCopy.getPayments().clear();
                userCopy.getAddresses().clear();
                userCopy.setDetails(null);
            }
            listCopy.add(userCopy);
        }

<<<<<<< HEAD
        return listCopy;
=======
        return Response.ok(listCopy).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
    }


    @POST
    @UnitOfWork
    @ApiOperation(value = "Creates a new user account", notes = "Adds the given user to the database. " +
            "User to be created must have at least one address and one payment option.",
            response = User.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid user data supplied"),
            @ApiResponse(code = 500, message = "Database error while creating user")})
    public Response addUser(@Valid User user, @Context UriInfo uriInfo) {

        if(dao.findByCredentials(user.getUsername()) != null) {
<<<<<<< HEAD
            throw new WebApplicationException("Username already taken", Status.BAD_REQUEST);
        }

=======
            return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorMessage("Username already taken")).build();
        }

        Response.ResponseBuilder response;

>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
        // null out IDs
        user.setId(null);

        for (Payment payment : user.getPayments()) {
            //set user property on payment (for hibernate)
            payment.setUser(user);
        }

        //set new password
        user.setPassword(util.hash(user.getPassword().toCharArray()));

        //set json ignored properties
        Date now = new Date();
        user.setCreated(now);
        user.setUpdated(now);

        User createdUser = this.dao.create(user);

<<<<<<< HEAD
        URI uri = uriInfo.getAbsolutePathBuilder().path(createdUser.getId().toString()).build();
        return Response.created(uri).entity(createdUser).build();
=======
        if (createdUser == null) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage("Database error"));

        } else { // user successfully created
            URI uri = uriInfo.getAbsolutePathBuilder().path(createdUser.getId().toString()).build();
            response = Response.created(uri).entity(createdUser);
        }

        return response.build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
    }

    @PUT
    @Path("/{userId}")
    @UnitOfWork
    @ApiOperation(value = "Updates the user account",
            notes = "Updates the given user's account information", response = User.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid user data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "User cannot update data for another user"),
            @ApiResponse(code = 404, message = "User ID not found")})
<<<<<<< HEAD
    public User updateUser(
=======
    public Response updateUser(
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
            @ApiParam(value = "ID of a user", required = true) @PathParam("userId") LongParam userId,
            @Valid User user, @ApiParam(hidden = true) @Auth User loggedInUser) {

        // FORBIDDEN TO UPDATE IF USER LOGGED IN IS NOT THE SAME
        if (!userId.get().equals(loggedInUser.getId())) {
<<<<<<< HEAD
            throw new WebApplicationException("You are not logged in as the user you are trying to update", Status.FORBIDDEN);
        }

        User found = this.dao.findById(userId.get());
=======
            return Response.status(Response.Status.FORBIDDEN).entity(new ErrorMessage("User cannot update data for another user")).build();
        }

        // NOT FOUND
        User found = this.dao.findById(userId.get());
        if (found == null) {
            Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage("User id not found")).build();
        }
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc

        Map<Address, Address> addressMap = new HashMap<>();
        for(Address address : found.getAddresses()) {
            addressMap.put(address, address);
        }

        Map<Payment, Payment> paymentMap = new HashMap<>();
        for(Payment payment : found.getPayments()) {
            paymentMap.put(payment, payment);
        }

        //set IDs
        user.getDetails().setId(found.getDetails().getId());
        for(Address address : user.getAddresses()) {
            if(addressMap.containsKey(address)) {
                address.setId(addressMap.get(address).getId());
            }
        }

        for(Payment payment : user.getPayments()) {
            if(paymentMap.containsKey(payment)) {
                payment.setId(paymentMap.get(payment).getId());
                payment.getAddress().setId(paymentMap.get(payment).getAddress().getId());
            }
            //set user property on payment (for hibernate)
            payment.setUser(user);
        }

        // set json ignored properties
        user.setId(found.getId());
        user.setCreated(found.getCreated());
        user.setUpdated(new Date());
        user.setRating(found.getRating());

        User updatedUser = this.dao.update(user);

<<<<<<< HEAD
        return updatedUser;
=======
        return Response.ok(updatedUser).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
    }

    @DELETE
    @Path("/{userId}")
    @UnitOfWork
    @ApiOperation(value = "Deletes user account", notes = "Deletes the account with given user ID")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "User successfully deleted"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Logged in user cannot delete another user"),
            @ApiResponse(code = 404, message = "User ID not found")})
    public Response deleteUser(
            @ApiParam(value = "ID of a user", required = true) @PathParam("userId") LongParam userId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        // FORBIDDEN TO DELETE IF USER LOGGED IN IS NOT THE SAME
        if (!userId.get().equals(loggedInUser.getId())) {
<<<<<<< HEAD
            throw new WebApplicationException("You are not logged in as the user you are trying to delete", Status.FORBIDDEN);
=======
            return Response.status(Response.Status.FORBIDDEN).entity(new ErrorMessage("Logged in user cannot delete another user")).build();
        }

        // userId doesn't exist
        if (this.dao.findById(userId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).entity(new ErrorMessage("User ID not found")).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
        }

        // user account successfully deleted
        this.dao.deleteUser(userId.get());
<<<<<<< HEAD
        return Response.status(Status.NO_CONTENT).build();
=======
        return Response.status(Response.Status.NO_CONTENT).entity(new ErrorMessage("User successfully deleted")).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
    }

    @GET
    @Path("/{userId}/items")
    @UnitOfWork
    @ApiOperation(value = "Finds the user's items",
            notes = "Returns all user items for the logged in user and active items for another user",
            response = Item.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No user items found"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 500, message = "Database error")})
<<<<<<< HEAD
    public List<Item> getItemsForUser(
            @ApiParam(value = "ID of a user", required = true) @PathParam("userId") LongParam userId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        List<Item> items = null;

=======
    public Response getItemsForUser(
            @ApiParam(value = "ID of a user", required = true) @PathParam("userId") LongParam userId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {
        List<Item> items = null;
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
        if (loggedInUser.getId().equals(userId.get())) {
            items = this.itemDAO.getAllItems(userId.get());
        } else {
            items = this.itemDAO.getActiveItems(userId.get());
        }

<<<<<<< HEAD
        if (items.isEmpty()) {
            throw new WebApplicationException("No items found", Status.NO_CONTENT);
        }

        return items;
=======
        if (items == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorMessage("Database error")).build();
        }
        if (items.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).entity(new ErrorMessage("No user items found")).build();
        }

        return Response.ok(items).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
    }

    @GET
    @Path("/{userId}/bids")
    @UnitOfWork
    @ApiOperation(value = "Finds the user's bids", notes = "Returns all user bids",
            response = Bid.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No user bids found"),
            @ApiResponse(code = 401, message = "User must be signed in")})
<<<<<<< HEAD
    public List<Bid> getBidsForUser(
=======
    public Response getBidsForUser(
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
            @ApiParam(value = "ID of a user", required = true) @PathParam("userId") LongParam userId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        List<Bid> list = bidDAO.getBidsForUser(userId.get());

<<<<<<< HEAD
        if (list.isEmpty()) {
            throw new WebApplicationException("No items found", Status.NO_CONTENT);
        }

        return list;
=======
        if (list == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if (list.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(list).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
    }

    // seller feedback by user
    @GET
    @Path("/{userId}/feedback/{feedbackId}")
    @UnitOfWork
    @ApiOperation(value = "Finds the user's feedback as seller or buyer.", notes = "Returns all user feedback. Valid IDs are 'seller' & 'buyer'",
            response = Feedback.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No user feedback found"),
            @ApiResponse(code = 401, message = "User must be signed in")})
<<<<<<< HEAD
    public List<Feedback> getSellerFeedback(
=======
    public Response getSellerFeedback(
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
            @ApiParam(value = "User ID", required = true) @PathParam("userId") LongParam userId,
            @ApiParam(value = "Feedback ID of a user",
                    required = true) @PathParam("feedbackId") NonEmptyStringParam feedbackId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {
<<<<<<< HEAD

=======
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
        List<Feedback> feedbackList = null;

        if(feedbackId.equals(Feedback.SELLER)) {
            feedbackList = feedbackDAO.getFeedbackForUser(userId.get(), Feedback.SELLER);
        }
        else if(feedbackId.equals(Feedback.BUYER)) {
            feedbackList = feedbackDAO.getFeedbackForUser(userId.get(), Feedback.BUYER);
        }
        else {
<<<<<<< HEAD
            throw new WebApplicationException("Invalid ID. Valid IDs are 'buyer' and 'seller'", Status.BAD_REQUEST);
        }

        if(feedbackList.isEmpty()) {
            throw new WebApplicationException("No feedback found", Status.NO_CONTENT);
        }

        return feedbackList;
=======
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(feedbackList == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        else if(feedbackList.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }

        return Response.ok(feedbackList).build();
>>>>>>> c2b4d56329ab5bdfb9471243e6b1ff4baa0c5abc
    }
}
