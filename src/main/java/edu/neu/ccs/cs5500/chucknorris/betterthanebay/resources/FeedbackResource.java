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
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;

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
    public Response getFeedback(@PathParam("itemId") LongParam itemId, @PathParam("feedbackId") NonEmptyStringParam feedbackId,
                                @Auth User loggedInUser) {

        Feedback feedback = dao.findById(feedbackId.get().get());

        if (feedback == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(feedback).build();
    }

    @POST
    public Response addFeedback(@PathParam("itemId") LongParam itemId, @Valid Feedback feedback, @Auth User loggedInUser) {

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
    public Response updateFeedback(@PathParam("itemId") LongParam feedbackId, @PathParam("feedbackId") NonEmptyStringParam bidId,
                                   @Valid Feedback feedback, @Auth User loggedInUser) {
//        ResponseBuilder response;
//
//        // authenticate seller
//
//        if(feedbackId.get().isPresent()) {
//            Feedback feedback1;// = dao.findById(itemId, feedbackId.get().get()); // check if equals "buyer" or "seller"
//        }
//        if (dao.findById(feedbackId.get().get()) == null) {
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        Feedback updatedFeedback = dao.update(feedback);
//
//        if (updatedFeedback == null) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
//        return Response.ok(updatedFeedback).build();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }

    @DELETE
    @Path("/{feedbackId}")
    public Response deleteFeedback(@PathParam("itemId") LongParam itemId, @PathParam("feedbackId") NonEmptyStringParam feedbackId,
                                   @Auth User loggedInUser) {

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
