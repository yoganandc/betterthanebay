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

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
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
    public Response getBid(@PathParam("itemId") LongParam itemId, @PathParam("bidId") LongParam bidId) {

        final Bid bid = dao.findById(bidId.get());
        if (bid == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(bid).build();
    }

    @GET
    public Response getAllBids(@PathParam("itemId") LongParam itemId) {

        final List<Bid> bids = null; //dao.findBidsForId(itemId.get());
        if (bids == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(bids).build();
    }

    @POST
    public Response addBid(@PathParam("itemId") LongParam itemId, @Valid Bid bid) {
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
    public Response updateBid(@PathParam("itemId") LongParam itemId, @PathParam("bidId") LongParam bidId, @Valid Bid bid) {

        // UNAUTHORIZED user

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
    public Response deleteBid(@PathParam("itemId") LongParam itemId, @PathParam("bidId") LongParam bidId) {

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