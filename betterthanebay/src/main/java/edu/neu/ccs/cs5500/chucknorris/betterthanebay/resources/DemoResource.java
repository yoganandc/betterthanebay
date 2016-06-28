package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/demo")
public class DemoResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String doGet() {
		return "Success!";
	}
}