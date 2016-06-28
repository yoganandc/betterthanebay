package edu.neu.ccs.cs5500.seattle.betterthanebay.resources;

import java.util.Date;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.seattle.betterthanebay.Item;


@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {
	
	
	@GET
	@Path("/{itemId}")
	public Item getItem() {
		
	}
	
	@GET
	public List<Item> getItems(@QueryParam("name") String name, @QueryParam("date") Date date) {
		
	}
	
	
	@POST
	public Response addItem(Item item) {
		
		
	}
	
	@PUT
	@Path("/{itemId}")
	public Response updateItem() {
		
	}
	
	@DELETE
	@Path("/{itemId}")
	public Response deleteItem() {
		
	}
	

}
