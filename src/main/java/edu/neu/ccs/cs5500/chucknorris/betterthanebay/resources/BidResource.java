package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.net.URI;
import java.util.ArrayList;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Payment;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "bid")
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
    @ApiOperation(value = "Find bid with given id",
            notes = "If {bidId} exists, returns the corresponding bid object", response = Bid.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Bid ID doesn't exist")})
    public Bid getBid(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @ApiParam(value = "Bid ID", required = true) @PathParam("bidId") LongParam bidId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        final Bid bid = this.dao.findById(bidId.get());

        if ((bid == null) || (bid.getItemId() != itemId.get())) {
            throw new WebApplicationException("Bid not found", Response.Status.NOT_FOUND);
        }

        Bid bidCopy = new Bid(bid);
        if(!bid.getUserId().equals(loggedInUser.getId())) {
            bidCopy.setPaymentId(null);
        }

        return bidCopy;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Find all bids for the item",
            notes = "Returns a list of all bids for the item", response = Bid.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No matching results found"),
            @ApiResponse(code = 401, message = "User must be logged in")})
    public List<Bid> getAllBids(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        final List<Bid> bids = dao.getBidsForItem(itemId.get());

        if (bids.isEmpty()) {
            throw new WebApplicationException("No bids found", Response.Status.NOT_FOUND);
        }

        List<Bid> bidsCopy = new ArrayList<>();
        for(Bid bid: bids) {
            Bid bidCopy = new Bid(bid);
            if(bid.getUserId() != loggedInUser.getId()) {
                bidCopy.setPaymentId(null);
            }
            bidsCopy.add(bidCopy);
        }

        return bidsCopy;
    }

    @POST
    @UnitOfWork
    @ApiOperation(value = "Creates a new bid",
            notes = "Places the given bid for the given item's auction", response = Bid.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid bid data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403,
                    message = "Item is not under auction or user may not place this bid"),
            @ApiResponse(code = 404, message = "Item not found")})
    public Response addBid(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @Valid Bid bid, @ApiParam(hidden = true) @Auth User loggedInUser, @Context UriInfo uriInfo) {

        Item item = this.itemDAO.findById(itemId.get());

        if (item == null) {
            throw new WebApplicationException("Item with given ID not found", Response.Status.NOT_FOUND);
        }

    /* user cannot bid on their own item */
        if (item.getUserId().equals(loggedInUser.getId())) {
            throw new WebApplicationException("You cannot bid on your own item", Response.Status.FORBIDDEN);
        }

    /* item must be under auction */
        Date now = new Date();
        if (item.getEndDate().before(now) || item.getStartDate().after(now)) {
            throw new WebApplicationException("Item is not under auction", Response.Status.FORBIDDEN);
        }

    /* bid amount must be greater than initial price and current bid */
        Bid currentWinning = dao.getCurrentWinningBid(itemId.get());

        if (currentWinning == null) {
            if (item.getInitialPrice().compareTo(bid.getAmount()) > 0) {
                throw new WebApplicationException("Bid is lower than minimum amount", Response.Status.BAD_REQUEST);
            }
        } else if (currentWinning.getAmount().compareTo(bid.getAmount()) > 0) {
            throw new WebApplicationException("Bid is lower than minimum amount", Response.Status.BAD_REQUEST);
        }

        boolean validPayment = false;
        for(Payment p : loggedInUser.getPayments()) {
            if(bid.getPaymentId().equals(p.getId())) {
                validPayment = true;
            }
        }

        if(!validPayment) {
            throw new WebApplicationException("Payment not found", Response.Status.BAD_REQUEST);
        }

        // set ID to null (as with all POST)
        bid.setId(null);

        // set json ignored properties
        bid.setItemId(itemId.get());
        bid.setUserId(loggedInUser.getId());
        bid.setTime(now);

        Bid createdBid = this.dao.create(bid);
        URI uri = uriInfo.getAbsolutePathBuilder().path(createdBid.getId().toString()).build();
        return Response.created(uri).entity(createdBid).build();
    }

    @PUT
    @Path("/{bidId}")
    @UnitOfWork
    @ApiOperation(value = "Updates bid details",
            notes = "Updates the given bid's details for the logged in user", response = Bid.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid bid data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403,
                    message = "Item is no longer under auction or user may not update this bid"),
            @ApiResponse(code = 404, message = "Bid ID not found")})
    public Bid updateBid(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @ApiParam(value = "Bid ID", required = true) @PathParam("bidId") LongParam bidId,
            @Valid Bid bid, @ApiParam(hidden = true) @Auth User loggedInUser) {

        Bid found = this.dao.findById(bidId.get());
        if (found == null) {
            throw new WebApplicationException("Bid not found", Response.Status.NOT_FOUND);
        }

        if (!found.getItemId().equals(itemId.get())) {
           throw new WebApplicationException("Bid not found", Response.Status.NOT_FOUND);
        }

    /* user cannot update bid of another user */
        if (!found.getUserId().equals(loggedInUser.getId())) {
            throw new WebApplicationException("You did not post this bid", Response.Status.FORBIDDEN);
        }

        boolean validPayment = false;
        for(Payment p : loggedInUser.getPayments()) {
            if(bid.getPaymentId().equals(p.getId())) {
                validPayment = true;
            }
        }

        if(!validPayment) {
            throw new WebApplicationException("Invalid payment ID", Response.Status.BAD_REQUEST);
        }

    /* user cannot update anything but payment option */
        bid.setAmount(found.getAmount());

        // set json ignored properties
        bid.setId(found.getId());
        bid.setItemId(itemId.get());
        bid.setUserId(loggedInUser.getId());
        bid.setTime(found.getTime());

        Bid updatedBid = this.dao.update(bid);

        return updatedBid;
    }

    @DELETE
    @Path("/{bidId}")
    @UnitOfWork
    @ApiOperation(value = "Deletes bid", notes = "Deletes the bid with given bid ID")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Bid successfully deleted"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Bid does not belong to signed in user"),
            @ApiResponse(code = 404, message = "Bid ID not found")})
    public Response deleteBid(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @ApiParam(value = "Bid ID", required = true) @PathParam("bidId") LongParam bidId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        Bid bid = this.dao.findById(bidId.get());

        if (bid == null) {
            throw new WebApplicationException("Bid not found", Response.Status.NOT_FOUND);
        }
        if (!bid.getItemId().equals(itemId.get())) {
            throw new WebApplicationException("Bid not found", Response.Status.NOT_FOUND);
        }

    /* user cannot delete bid of another user */
        if (!loggedInUser.getId().equals(bid.getUserId())) {
            throw new WebApplicationException("Not your bid to delete", Response.Status.BAD_REQUEST);
        }

        boolean success = this.dao.deleteBid(bidId.get());
        return Response.status(Response.Status.NO_CONTENT).build(); // bid successfully deleted
    }
}
