package edu.neu.ccs.cs5500.seattle.betterthanebay.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.seattle.betterthanebay.Feedback;

@Path("/feedbacks") 
@Produces(MediaType.APPLICATION_JSON)
public class FeedbackResource {
	
	private final User user;
	
	//all feedback
	@GET
	public List<Feedback> getFeedbacks() {
		return feebackService.getAllFeedback();
		
	}
	
	//feedback by id
	@GET
	@Path("/{feedbackId}")
	public Feedback getFeedback(@PathParam("feedbackId") long feedbackId) {
		return feedbackService.getFeedback(feedbackId);
		
	}
	
	//feedback by user
	@GET
	public List<Feedback> getFeedback(User user) {
		return feedbackService.getFeedback(user);
		
	}
	
	//feedback by item
	@GET
	public List<Feedback> getFeedback(Item item) {
		return feedbackService.getFeedback(item);
		
	}
	
	
	@POST
	public Response addFeedback() {
		
	}
	
	@PUT
	@Path("/{feedbackId}")
	public Response updateFeedback() {
		
	}
	
	@DELETE
	@Path("/{feedbackId}")
	public Response deleteFeedback() {
		
	}

}
