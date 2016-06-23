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

import edu.neu.ccs.cs5500.seattle.betterthanebay.Bid;

@Path("/bids")
@Produces(MediaType.APPLICATION_JSON)
public class BidResource {
	
	//all bids
	@GET
	public List<Bid> getBids() {
		return bidService.getAllBids();
	}
	
	//bid by id
	@GET
	@Path("/{bidId}")
	public Bid getBid(@PathParam("bidId") long bidId) {
		return bidService.getBid(bidId);
		
	}
	
	//bids by item
	@GET
	public List<Bid> getBids(Item item) {
		return bidService.getBids(item);
	}
	
	
	@POST
	public Response addBid() {
		
	}
	
	@PUT
	@Path("/{bidId}")
	public Response updateBid() {
		
	}
	
	@DELETE
	@Path("/{bidId}")
	public Response deleteBid() {
		
	}

}
