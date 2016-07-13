package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.util.List;
import java.util.Optional;

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

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Feedback;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import io.dropwizard.jersey.params.LongParam;

@Path("/feedback")
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackResource {

    private FeedbackDAO dao;

    public FeedbackResource(FeedbackDAO dao) {
        super();
        this.dao = dao;
    }

    // feedback by id
    @GET
    @Path("/{feedbackId}")
    public Response getFeedback(@PathParam("feedbackId") LongParam feedbackId) {

        Optional<Feedback> feedback = dao.findById(feedbackId.get());

        if (feedback == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(feedback).build();

    }

    @POST
    public Response addFeedback(@Valid Feedback feedback) {

        ResponseBuilder response;

        if (feedback.getRating() == null) {
            response = Response.status(Response.Status.BAD_REQUEST);

        } else {
            Feedback createdFeedback = null; //dao.createFeedback(feedback);
            if (createdFeedback == null) {
                response = Response.status(Response.Status.BAD_REQUEST); // failure
                //
            } else {
                response = Response.status(Response.Status.CREATED);
                // response -> add feedback data
                // update user rating
            }
        }

        return response.build();
    }

    @PUT
    @Path("/{feedbackId}")
    public Response updateFeedback(@PathParam("feedbackId") LongParam bidId, @Valid Feedback feedback) {
        ResponseBuilder response;

        // authenticate seller

        response = Response.status(Response.Status.OK); // successfully updated
        // else
        response = Response.status(Response.Status.BAD_REQUEST); // failure || invalid data || not found

        return response.build();
    }

    @DELETE
    @Path("/{feedbackId}")
    public Response deleteFeedback(@PathParam("feedbackId") LongParam feedbackId) {

        // authenticate seller || buyer

        ResponseBuilder response;

        Feedback feedback = null; //dao.getFeedback(feedbackId);
        if (feedback == null) {
            response = Response.status(Response.Status.BAD_REQUEST); // invalid bid id
        }

        boolean success = false; //dao.deleteFeedback(feedbackId);
        if (success) {
            response = Response.status(Response.Status.OK); // feedback successfully deleted
        } else {
            response = Response.status(Response.Status.BAD_REQUEST); // failure
        }

        return response.build();

    }

    // seller feedback by user
    @GET
    @Path("/users/{userId}/sellerFeedback")
    public Response getSellerFeedback(@PathParam("userId") LongParam userId) {

        // if valid user
        return null; //dao.getSellerFeedback(userId);
    }

    // buyer feedback by user
    @GET
    @Path("/users/{userId}/buyerFeedback")
    public Response getBuyerFeedback(@PathParam("userId") LongParam userId) {

        // if valid user id
        return null; //dao.getBuyerFeedback(userId);
    }

    // feedback by item
    @GET
    @Path("/items/{itemId}/feedback")
    public Response getFeedback(Item item) {

        // if item == null or feedback id does not exist ---- BAD REQUEST

        long itemId = item.getId();
        // if id exists --------
        return null; //dao.getFeedback(item);

    }

    // seller feedback by item
    @GET
    @Path("/items/{itemId}/feedback/seller")
    public Response getSellerFeedback(@Valid Item item) {

        // if item == null or feedback id does not exist ---- BAD REQUEST


        long itemId = item.getId();
        // if id exists --------
        return null; //dao.getSellerFeedback(item);

    }

    // buyer feedback by item
    @GET
    @Path("/items/{itemId}/feedback/buyer")
    public Response getBuyerFeedback(@Valid Item item) {

        // if item == null or feedback id does not exist ---- BAD REQUEST

        long itemId = item.getId();
        // if id exists --------
        return null; //dao.getBuyerFeedback(item);

    }
}
