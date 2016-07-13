package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;
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

@Path("/bids")
@Produces(MediaType.APPLICATION_JSON)
public class BidResource {

    private BidDAO dao;

    public BidResource(BidDAO dao) {
        super();
        this.dao = dao;
    }

    @GET
    @Path("/{bidId}")
    public Response getBid(@PathParam("bidId") LongParam bidId) {

        Long id = bidId.get();
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        final Bid bid = null; //dao.getById(bidId.get());
        if (bid == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(bid).build();

    }

    // all bids
//    @GET
//    public List<Bid> getBids() {
//
//        return null; //dao.getAllBids();
//    }

    // bids by item
//    @GET
//    @Path("/items/{itemId}/bids") /// remove path before class name?
//    public List<Bid> getBids(Item item) {
//        // if item == null or bid id does not exist ----
//        long itemId = item.getId();
//
//        // if id exists --------
//        return null; //dao.getBids(item);
//
//    }

    // bids by user
//    @GET
//    @Path("/users/{userId}/bids")
//    public List<Bid> getBids(User user) {
//        // if user == null or user id does not exist ----
//
//        long userId = user.getId();
//
//        // if id exists ------------------
//        return null; //bidDAO.getBids(user);
//    }

    // all bids by user for a specific item
    // "/users/{userId}/items/{itemId}/bids"

    @POST
    public Response addBid(@Valid Bid bid) {
        ResponseBuilder response;

        Bid createdBid = null; //dao.createBid(bid);
        if (createdBid == null) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR); // failure
        } else {
            response = Response.created(URI.create("/bids/" + createdBid.getId())).entity(createdBid);
        }
        return response.build();
    }

    @PUT
    @Path("/{bidId}")
    public Response updateBid(@PathParam("bidId") LongParam bidId, Bid bid) {
        ResponseBuilder response;
        // UNAUTHORIZED user

        // if bid id exists ---------------

        // update data
        response = Response.status(Response.Status.OK); // successfully updated
        // else
        response = Response.status(Response.Status.BAD_REQUEST); // failure || invalid data || not found

        return response.build();

    }

    @DELETE
    @Path("/{bidId}")
    public Response deleteBid(@PathParam("bidId") long bidId) {
        ResponseBuilder response;

        // authenticate user
        // when can the user delete a bid

        Bid bid = null; //dao.getBid(bidId);
        if (bid == null) {
            response = Response.status(Response.Status.BAD_REQUEST); // invalid bid id
        }

        boolean success = false; //dao.deleteBid(bidId);
        if (success) {
            response = Response.status(Response.Status.OK); // bid successfully deleted
        } else {
            response = Response.status(Response.Status.BAD_REQUEST); // failure
        }

        return response.build();

    }

}