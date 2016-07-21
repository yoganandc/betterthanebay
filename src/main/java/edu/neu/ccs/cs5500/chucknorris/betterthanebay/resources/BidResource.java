package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
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
    public Response getBid(@PathParam("itemId") LongParam itemId, @PathParam("bidId") LongParam bidId, @Auth User loggedInUser) {

        final Bid bid = dao.findById(bidId.get());
        if (bid == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(bid).build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(
            value = "Find all bids for the user",
            notes = "Returns a list of all bids for the logged in user",
            response = Bid.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No matching results found"),
            @ApiResponse(code = 401, message = "User must be logged in")})
    public Response getAllBids(@PathParam("itemId") LongParam itemId, @Auth User loggedInUser) {

        final List<Bid> bids = null; //dao.findBidsForId(itemId.get());
        if (bids == null) {
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
            @ApiResponse(code = 403, message = "Item is not under auction")})
    public Response addBid(@PathParam("itemId") LongParam itemId, @Valid Bid bid, @Auth User loggedInUser) {
        ResponseBuilder response;

        if (itemDAO.findById(itemId.get()).getUserId() == loggedInUser.getId()) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        /*
        TODO
        check if item is under auction
        */

        Bid createdBid = dao.create(bid);
        if (createdBid == null) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR); // failure
        } else {
            response = Response.created(URI.create("/bids/" + createdBid.getId())).entity(createdBid);
        }
        return response.build();
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
            @ApiResponse(code = 403, message = "User cannot update bid data for another user"),
            @ApiResponse(code = 404, message = "Bid ID not found")})
    public Response updateBid(@PathParam("itemId") LongParam itemId, @PathParam("bidId") LongParam bidId, @Valid Bid bid,
                              @Auth User loggedInUser) {

        if (dao.findById(bidId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Bid updatedBid = dao.update(bid);  // dao.updateBid(bidId, bid);

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
    public Response deleteBid(@PathParam("itemId") LongParam itemId, @PathParam("bidId") LongParam bidId,
                              @Auth User loggedInUser) {

        /* TODO
        FORBIDDEN
        UNAUTHORIZED */

        if (dao.findById(bidId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // bidId doesn't exist
        }

        boolean success = false; //dao.deleteBid(bidId);
        if (success) {
            return Response.status(Response.Status.NO_CONTENT).build(); // bid successfully deleted
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // failure
        }
    }
}