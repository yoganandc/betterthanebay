package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.net.URI;
import java.util.Date;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BidResource {

    private BidDAO dao;
    private ItemDAO itemDAO;

    public BidResource(BidDAO dao, ItemDAO itemDAO) {
        super();
        this.dao = dao;
        this.itemDAO = itemDAO;
    }


    @GET
    @Path("/{bidId}")
    @UnitOfWork
    @ApiOperation(
            value = "Find bid with given id",
            notes = "If {bidId} exists, returns the corresponding bid object",
            response = Bid.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Bid ID doesn't exist")})
    public Response getBid(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                           @ApiParam(value = "Bid ID", required = true) @PathParam("bidId") LongParam bidId,
                           @Auth User loggedInUser) {

        final Bid bid = dao.findById(bidId.get());

        if (bid == null || bid.getItemId() != itemId.get()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(bid).build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(
            value = "Find all bids for the item",
            notes = "Returns a list of all bids for the item",
            response = Bid.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No matching results found"),
            @ApiResponse(code = 401, message = "User must be logged in")})
    public Response getAllBids(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                               @Auth User loggedInUser) {

        final List<Bid> bids = null; //dao.findBidsForItem(itemId.get());
        if (bids.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(bids).build();
    }

    @POST
    @UnitOfWork
    @ApiOperation(
            value = "Creates a new bid",
            notes = "Places the given bid for the given item's auction",
            response = Bid.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid bid data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Item is not under auction or user may not place this bid"),
            @ApiResponse(code = 404, message = "Item not found")})
    public Response addBid(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                           @Valid Bid bid, @Auth User loggedInUser) {

        Item item = itemDAO.findById(itemId.get());

        if(item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        /* user cannot bid on their own item */
        if (item.getUserId().equals(loggedInUser.getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        /* item must be under auction */
        Date now = new Date();
        if (item.getEndDate().before(now) || item.getStartDate().after(now)) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        /* bid amount must be greater than initial price and current bid */
        Bid currentWinning = null; //dao.getCurrentWinningForItem(Long itemId)

        if (currentWinning == null) {
            if (item.getInitialPrice().compareTo(bid.getAmount()) > 0) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        } else if (currentWinning.getAmount().compareTo(bid.getAmount()) > 0) {
                return Response.status(Response.Status.BAD_REQUEST).build();
        }

        /* TODO */
        // VERIFY THAT PAYMENT ID IS VALID AND BELONGS TO USER

        //set ID to null (as with all POST)
        bid.setId(null);

        //set json ignored properties
        bid.setItemId(itemId.get());
        bid.setUserId(loggedInUser.getId());
        bid.setTime(now);

        Bid createdBid = dao.create(bid);
        if (createdBid == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else {
            return Response.created(URI.create("/bids/" + createdBid.getId())).entity(createdBid).build();
        }
    }

    @PUT
    @Path("/{bidId}")
    @UnitOfWork
    @ApiOperation(
            value = "Updates bid details",
            notes = "Updates the given bid's details for the logged in user",
            response = Bid.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid bid data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Item is no longer under auction or user may not update this bid"),
            @ApiResponse(code = 404, message = "Bid ID not found")})
    public Response updateBid(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                              @ApiParam(value = "Bid ID", required = true) @PathParam("bidId") LongParam bidId,
                              @Valid Bid bid, @Auth User loggedInUser) {

        //bad request if entity's id does not match path id
        if(!bidId.get().equals(bid.getId())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Bid found = dao.findById(bidId.get());
        if(found == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if(!found.getItemId().equals(itemId.get())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        /* user cannot update bid of another user */
        if (!found.getUserId().equals(loggedInUser.getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        /* TODO */
        // VERIFY THAT PAYMENT ID IS VALID AND BELONGS TO USER

        /* user cannot update anything but payment option */
        bid.setAmount(found.getAmount());

        //set json ignored properties
        bid.setItemId(itemId.get());
        bid.setUserId(loggedInUser.getId());
        bid.setTime(found.getTime());

        Bid updatedBid = dao.update(bid);

        if (updatedBid == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(updatedBid).build();
    }

    @DELETE
    @Path("/{bidId}")
    @UnitOfWork
    @ApiOperation(
            value = "Deletes bid",
            notes = "Deletes the bid with given bid ID")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Bid successfully deleted"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Bid does not belong to signed in user"),
            @ApiResponse(code = 404, message = "Bid ID not found")})
    public Response deleteBid(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                              @ApiParam(value = "Bid ID", required = true) @PathParam("bidId") LongParam bidId,
                              @Auth User loggedInUser) {

        Bid bid = dao.findById(bidId.get());

        if (bid == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // bidId doesn't exist
        }
        if(!bid.getItemId().equals(itemId.get())) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        /* user cannot delete bid of another user */
        if (!loggedInUser.getId().equals(bid.getUserId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        boolean success = dao.deleteBid(bidId.get());
        return Response.status(Response.Status.NO_CONTENT).build(); // bid successfully deleted
    }
}