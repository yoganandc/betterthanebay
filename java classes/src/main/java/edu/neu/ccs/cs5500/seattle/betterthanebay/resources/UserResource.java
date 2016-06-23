package edu.neu.ccs.cs5500.seattle.betterthanebay.resources;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.seattle.betterthanebay.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
	
//	private UserDAO dao;
//	
//	public UserResource(UserDAO dao) {
//		super();
//		this.dao = dao;
//	}

	
	@GET
	@Path("/{userId}")
	public User getUser(@PathParam("userId") long userId) {
		
		return dao.getUser(userId);		
	}
	
	@GET
	public List<User> getUser(@QueryParam("username") String username) {
		if (username != null && !username.isEmpty()) {
			// return by user
		}
		// return all users
	}
	
	
	@POST
	public Response addUser(User user) {
		return dao.addUser(user);
		
	}
	
	@PUT
	@Path("/{userId}")
	public Response updateUser(@PathParam("userId") long userId, User user) {
		
	}
	
	@DELETE
	@Path("/{userId}")
	public Response deleteUser(@PathParam("userId") long userId) {
		
		dao.deleteUser(userId);
	}

}
