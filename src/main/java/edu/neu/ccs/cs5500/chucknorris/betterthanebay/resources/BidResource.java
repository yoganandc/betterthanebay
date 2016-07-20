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
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BidResource {

    private BidDAO dao;

    public BidResource(BidDAO dao) {
        super();
        this.dao = dao;
    }

    @GET
    @Path("/{bidId}")
    @UnitOfWork
    @ApiOperation(
            value = "Find bid with given id",
            notes = "If {bidId} exists, returns the corresponding bid object",
            response = Bid.class)
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
    public Response addBid(@PathParam("itemId") LongParam itemId, @Valid Bid bid, @Auth User loggedInUser) {
        ResponseBuilder response;

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
    public Response deleteBid(@PathParam("itemId") LongParam itemId, @PathParam("bidId") LongParam bidId,
                              @Auth User loggedInUser) {

        /* TO DO
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