package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.util.List;
import java.util.Optional;

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

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Feedback;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import io.dropwizard.jersey.params.LongParam;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FeedbackResource {

    private FeedbackDAO dao;

    public FeedbackResource(FeedbackDAO dao) {
        super();
        this.dao = dao;
    }

    // feedback by id
    @GET
    @Path("/{feedbackId}")
    public Response getFeedback(@PathParam("itemId") LongParam itemId, @PathParam("feedbackId") LongParam feedbackId) {

        Feedback feedback = dao.findById(feedbackId.get());

        if (feedback == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(feedback).build();
    }

    @POST
    public Response addFeedback(@PathParam("itemId") LongParam itemId, @Valid Feedback feedback) {

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
    public Response updateFeedback(@PathParam("itemId") LongParam itemId, @PathParam("feedbackId") LongParam bidId, @Valid Feedback feedback) {
        ResponseBuilder response;

        // authenticate seller

        response = Response.status(Response.Status.OK); // successfully updated
        // else
        response = Response.status(Response.Status.BAD_REQUEST); // failure || invalid data || not found

        return response.build();
    }

    @DELETE
    @Path("/{feedbackId}")
    public Response deleteFeedback(@PathParam("itemId") LongParam itemId, @PathParam("feedbackId") LongParam feedbackId) {

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
}
