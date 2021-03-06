package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.*;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

/*
 * This can be confusing!
 * Buyer leaves feedback for seller at /item/{itemId}/feedback/seller
 * Seller leaves feedback for buyer at /item/{itemId}/feedback/buyer
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "feedback")
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
            value = "Find feedback left for item's buyer or seller",
            notes = "If {feedbackId} exists, returns the corresponding feedback object. Valid IDs are 'seller' & 'buyer'",
            response = Feedback.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Feedback not found")})
    public Feedback getFeedback(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                                @ApiParam(value = "Feedback ID", required = true) @PathParam("feedbackId") NonEmptyStringParam feedbackId,
                                @ApiParam(hidden = true) @Auth User loggedInUser) {

        if(!feedbackId.get().isPresent() || !VALID_ID.contains(feedbackId.get().get())) {
            throw new WebApplicationException("Valid ID is either \"seller\" or \"buyer\"", Response.Status.NOT_FOUND);
        }

        Feedback feedback = dao.findById(itemId.get(), feedbackId.get().get());

        if (feedback == null) {
            throw new WebApplicationException("No feedback found", Response.Status.NOT_FOUND);
        }

        return feedback;
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
            @ApiResponse(code = 404, message = "Item ID not found"),
            @ApiResponse(code = 500, message = "Database error while creating feedback")})
    public Response addFeedback(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                                @Valid Feedback feedback, @ApiParam(hidden = true) @Auth User loggedInUser, @Context UriInfo uriInfo) {

        Item item = itemDAO.findById(itemId.get());
        if(item == null) {
            throw new WebApplicationException("Item ID not found", Response.Status.NOT_FOUND);
        }

        // FIRST CHECK IF ITEM HAS FINISHED AUCTION
        Date now = new Date();
        if(item.getEndDate().after(now)) {
            throw new WebApplicationException("Item auction has not ended", Response.Status.BAD_REQUEST);
        }

        // NOW CHECK IF USER POSTED ITEM OR WON IT
        Bid bid = bidDAO.getCurrentWinningBid(item.getId());

        if (bid == null) {
            throw new WebApplicationException("Item auction has not ended", Response.Status.FORBIDDEN);
        } else if (!bid.getUserId().equals(loggedInUser.getId()) && !item.getUserId().equals(loggedInUser.getId())) {
            throw new WebApplicationException("Only seller or buyer of the item can leave feedback", Response.Status.FORBIDDEN);
        }

        //set id to null
        feedback.setId(null);

        //set json ignore properties
        feedback.setItemId(item.getId());
        feedback.setCreated(now);
        feedback.setUpdated(now);
        if(bid.getUserId().equals(loggedInUser.getId())) {
            feedback.setUserId(item.getUserId());
        }
        else {
            feedback.setUserId(bid.getUserId());
        }

        Feedback created = null;

        // if feedback is being left by buyer, it is for the seller (slightly confusing!)
        if(bid.getUserId().equals(loggedInUser.getId())) {
            created = dao.create(feedback, Feedback.SELLER);
        }
        else {
            created = dao.create(feedback, Feedback.BUYER);
        }

        if (bid.getUserId().equals(loggedInUser.getId())) {
            return Response.created(uriInfo.getAbsolutePathBuilder().path(Feedback.SELLER).build())
                    .entity(created)
                    .build();
        } else {
            return Response.created(uriInfo.getAbsolutePathBuilder().path(Feedback.BUYER).build())
                    .entity(created)
                    .build();
        }
    }

    @PUT
    @Path("/{feedbackId}")
    @UnitOfWork
    @ApiOperation(
            value = "Updates feedback details",
            notes = "Updates the given feedback's details for the logged in user. Valid IDs are 'seller' & 'buyer'",
            response = Feedback.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid feedback data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "User cannot update feedback data for another user"),
            @ApiResponse(code = 404, message = "Feedback not found"),
            @ApiResponse(code = 500, message = "Database error")})
    public Feedback updateFeedback(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                                   @ApiParam(value = "Feedback ID", required = true) @PathParam("feedbackId") NonEmptyStringParam feedbackId,
                                   @Valid Feedback feedback, @ApiParam(hidden = true) @Auth User loggedInUser) {

        if(!feedbackId.get().isPresent() || !VALID_ID.contains(feedbackId.get().get())) {
            throw new WebApplicationException("Valid ID is either \"seller\" or \"buyer\"", Response.Status.NOT_FOUND);
        }

        // get associated item
        Item item = itemDAO.findById(itemId.get());
        if(item == null) {
            throw new WebApplicationException("Item with given ID not found", Response.Status.NOT_FOUND);
        }

        // get associated winning bid
        Bid bid = bidDAO.getCurrentWinningBid(item.getId());

        // check if user accessing feedback is buyer (for seller, i.e., at path /seller)
        if(feedbackId.get().get().equals(Feedback.SELLER) &&!loggedInUser.getId().equals(bid.getUserId())) {
            throw new WebApplicationException("Seller cannot update their own feedback", Response.Status.FORBIDDEN);
        }

        //check if user accessing is seller (for buyer, i.e., at path /buyer)
        if(feedbackId.get().get().equals(Feedback.BUYER) &&!loggedInUser.getId().equals(item.getUserId())) {
            throw new WebApplicationException("Buyer cannot update their own feedback", Response.Status.FORBIDDEN);
        }

        Feedback found = dao.findById(itemId.get(), feedbackId.get().get());
        if(found == null) {
            throw new WebApplicationException("Feedback not found", Response.Status.NOT_FOUND);
        }

        // set json ignored properties
        feedback.setId(found.getId());
        feedback.setItemId(item.getId());
        feedback.setCreated(found.getCreated());
        feedback.setUpdated(new Date());
        feedback.setUserId(found.getUserId());

        Feedback updatedFeedback = null;

        if(feedbackId.get().get().equals(Feedback.SELLER)) {
            updatedFeedback = dao.update(feedback, Feedback.SELLER);
        }
        else {
            updatedFeedback = dao.update(feedback, Feedback.BUYER);
        }

        return updatedFeedback;
    }

    @DELETE
    @Path("/{feedbackId}")
    @UnitOfWork
    @ApiOperation(
            value = "Deletes feedback",
            notes = "Deletes the feedback with given feedback ID and corresponding item ID. Valid IDs are 'seller' & 'buyer'")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Feedback successfully deleted"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Feedback does not belong to signed in user"),
            @ApiResponse(code = 404, message = "Feedback not found")})
    public Response deleteFeedback(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                                   @ApiParam(value = "Feedback ID", required = true) @PathParam("feedbackId") NonEmptyStringParam feedbackId,
                                   @ApiParam(hidden = true) @Auth User loggedInUser) {

        if(!feedbackId.get().isPresent() || VALID_ID.contains(feedbackId.get().get())) {
            throw new WebApplicationException("Valid ID is either \"seller\" or \"buyer\"", Response.Status.NOT_FOUND);
        }

        // get associated item
        Item item = itemDAO.findById(itemId.get());
        if(item == null) {
            throw new WebApplicationException("Item with given ID not found", Response.Status.NOT_FOUND);
        }

        // get associated winning bid
        Bid bid = bidDAO.getCurrentWinningBid(item.getId());

        // check if user accessing feedback is buyer (for seller, i.e., at path /seller)
        if(feedbackId.get().get().equals(Feedback.SELLER) &&!loggedInUser.getId().equals(bid.getUserId())) {
            throw new WebApplicationException("Seller cannot delete feedback left by buyer", Response.Status.FORBIDDEN);
        }

        //check if user accessing is seller (for buyer, i.e., at path /buyer)
        if(feedbackId.get().get().equals(Feedback.BUYER) &&!loggedInUser.getId().equals(item.getUserId())) {
            throw new WebApplicationException("Buyer cannot delete feedback left by seller", Response.Status.FORBIDDEN);
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
            throw new WebApplicationException("Feedback not found", Response.Status.NOT_FOUND);
        }
    }
}
