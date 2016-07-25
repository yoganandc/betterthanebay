package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Feedback;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;

/*
 * This can be confusing!
 * Buyer leaves feedback for seller at /item/{itemId}/feedback/seller
 * Seller leaves feedback for buyer at /item/{itemId}/feedback/buyer
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeedbackResource {

    private FeedbackDAO dao;
    private ItemDAO itemDAO;
    private BidDAO bidDAO;
    private static final Set<String> VALID_ID = new HashSet<>();

    static {
        VALID_ID.add(Feedback.BUYER);
        VALID_ID.add(Feedback.SELLER);
    }

    public FeedbackResource(FeedbackDAO dao, ItemDAO itemDAO, BidDAO bidDAO) {
        super();
        this.dao = dao;
        this.itemDAO = itemDAO;
        this.bidDAO = bidDAO;
    }

    @GET
    @Path("/{feedbackId}")
    @UnitOfWork
    @ApiOperation(
            value = "Find feedback with given id",
            notes = "If {feedbackId} exists, returns the corresponding feedback object",
            response = Feedback.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Feedback not found")})
    public Response getFeedback(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                                @ApiParam(value = "Feedback ID", required = true) @PathParam("feedbackId") NonEmptyStringParam feedbackId,
                                @Auth User loggedInUser) {

        if(!feedbackId.get().isPresent() || VALID_ID.contains(feedbackId.get().get())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Feedback feedback = dao.findById(itemId.get(), feedbackId.get().get());

        if (feedback == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(feedback).build();
    }

    @POST
    @UnitOfWork
    @ApiOperation(
            value = "Creates a new feedback",
            notes = "Adds the given feedback to the given item's details",
            response = Feedback.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid feedback data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Forbidden user access or feedback already exists"),
            @ApiResponse(code = 404, message = "Item with given ID not found")})
    public Response addFeedback(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                                @Valid Feedback feedback, @Auth User loggedInUser) {

        Item item = itemDAO.findById(itemId.get());
        if(item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // FIRST CHECK IF ITEM HAS FINISHED AUCTION
        Date now = new Date();
        if(item.getEndDate().after(now)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        // NOW CHECK IF USER POSTED ITEM OR WON IT
        Bid bid = null; //bidDAO.getCurrentWinningForItem(item.getId());

        /* TODO */
        // currentWinningBidForItem could return null

        if(!bid.getUserId().equals(loggedInUser.getId()) && !item.getUserId().equals(loggedInUser.getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        //set id to null
        feedback.setId(null);

        //set json ignore properties
        feedback.setItemId(item.getId());
        feedback.setCreated(now);
        feedback.setUpdated(now);

        Feedback created = null;

        // if feedback is being left by buyer, it is for the seller (slightly confusing!)
        if(bid.getUserId().equals(loggedInUser.getId())) {
            created = dao.create(feedback, Feedback.SELLER);
        }
        else {
            created = dao.create(feedback, Feedback.BUYER);
        }

        if(created == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else {

            if (bid.getUserId().equals(loggedInUser.getId())) {
                return Response.created(URI.create("/items/" + item.getId() + "/feedback/" + Feedback.SELLER))
                        .entity(created)
                        .build();
            } else {
                return Response.created(URI.create("/items/" + item.getId() + "/feedback/" + Feedback.BUYER))
                        .entity(created)
                        .build();
            }
        }
    }

    @PUT
    @Path("/{feedbackId}")
    @UnitOfWork
    @ApiOperation(
            value = "Updates feedback details",
            notes = "Updates the given feedback's details for the logged in user",
            response = Feedback.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid feedback data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "User cannot update feedback data for another user"),
            @ApiResponse(code = 404, message = "Feedback not found")})
    public Response updateFeedback(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                                   @ApiParam(value = "Feedback ID", required = true) @PathParam("feedbackId") NonEmptyStringParam feedbackId,
                                   @Valid Feedback feedback, @Auth User loggedInUser) {

        if(!feedbackId.get().isPresent() || VALID_ID.contains(feedbackId.get().get())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // get associated item
        Item item = itemDAO.findById(itemId.get());
        if(item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // get associated winning bid
        Bid bid = null; // bidDAO.getCurrentWinningForItem(itemId.get());

        // check if user accessing feedback is buyer (for seller, i.e., at path /seller)
        if(feedbackId.get().get().equals(Feedback.SELLER) &&!loggedInUser.getId().equals(bid.getUserId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        //check if user accessing is seller (for buyer, i.e., at path /buyer)
        if(feedbackId.get().get().equals(Feedback.BUYER) &&!loggedInUser.getId().equals(item.getUserId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Feedback found = dao.findById(itemId.get(), feedbackId.get().get());
        if(found == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // WE MUST SET FEEDBACK ID IN CASE SUPPLIED FEEDBACK HAS
        // WRONG ID OR NO ID (THIS IS ONLY FOR FEEDBACK AS PATH DOES NOT
        // MATCH ID INSIDE OBJECT
        feedback.setId(found.getId());

        // set json ignored properties
        feedback.setItemId(item.getId());
        feedback.setCreated(found.getCreated());
        feedback.setUpdated(new Date());

        Feedback updatedFeedback = null;

        if(feedbackId.get().get().equals(Feedback.SELLER)) {
            updatedFeedback = dao.update(feedback, Feedback.SELLER);
        }
        else {
            updatedFeedback = dao.update(feedback, Feedback.BUYER);
        }

        if (updatedFeedback == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(updatedFeedback).build();
    }

    @DELETE
    @Path("/{feedbackId}")
    @UnitOfWork
    @ApiOperation(
            value = "Deletes feedback",
            notes = "Deletes the feedback with given feedback ID and corresponding item ID")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Feedback successfully deleted"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Feedback does not belong to signed in user"),
            @ApiResponse(code = 404, message = "Feedback not found")})
    public Response deleteFeedback(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                                   @ApiParam(value = "Feedback ID", required = true) @PathParam("feedbackId") NonEmptyStringParam feedbackId,
                                   @Auth User loggedInUser) {

        if(!feedbackId.get().isPresent() || VALID_ID.contains(feedbackId.get().get())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // get associated item
        Item item = itemDAO.findById(itemId.get());
        if(item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // get associated winning bid
        Bid bid = null; // bidDAO.getCurrentWinningForItem(itemId.get());

        // check if user accessing feedback is buyer (for seller, i.e., at path /seller)
        if(feedbackId.get().get().equals(Feedback.SELLER) &&!loggedInUser.getId().equals(bid.getUserId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        //check if user accessing is seller (for buyer, i.e., at path /buyer)
        if(feedbackId.get().get().equals(Feedback.BUYER) &&!loggedInUser.getId().equals(item.getUserId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        boolean success = false;

        if(feedbackId.get().get().equals(Feedback.SELLER)) {
            success = dao.deleteFeedback(itemId.get(), Feedback.SELLER);
        }
        else {
            success = dao.deleteFeedback(itemId.get(), Feedback.BUYER);
        }

        if (success) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

    }
}
