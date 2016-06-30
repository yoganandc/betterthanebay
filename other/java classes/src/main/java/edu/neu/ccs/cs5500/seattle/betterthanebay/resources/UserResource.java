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
import javax.ws.rs.core.Response.ResponseBuilder;

import edu.neu.ccs.cs5500.seattle.betterthanebay.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

  // private UserDAO dao;
  //
  // public UserResource(UserDAO dao) {
  // super();
  // this.dao = dao;
  // }

  // get user by userId
  @GET
  @Path("/{userId}")
  public User getUser(@PathParam("userId") long userId) {

    return dao.getUser(userId);
  }

  // query by user name or get all users if query parameter is blank
  @GET
  public List<User> getUser(@QueryParam("username") String username) {
    if ((username != null) && !username.isEmpty()) {
      // return by user
      return dao.getByUserName(username);
    }
    // return all users
    return dao.getAllUsers();
  }


  @POST
  public Response addUser(User user) {

    ResponseBuilder response;

    // FORBIDDEN if already logged in as another user

    if ((user.getUserName() == null) || (user.getPassword() != null)
        || (user.getPerson() != null)) {
      response = Response.status(Response.Status.BAD_REQUEST);

    } else {
      User createdUser = dao.createUser(user); /// if createUser returns user
      if (createdUser == null) {
        response = Response.status(Response.Status.BAD_REQUEST);
        //
      } else {
        response = Response.status(Response.Status.CREATED);
        // add user data
      }

    }

    return response.build();
  }

  @PUT
  @Path("/{userId}")
  public Response updateUser(@PathParam("userId") long userId, User user) {
    ResponseBuilder response;
    if (user.getId().equals(userId)) { // validate userId ??
      response = Response.status(Response.Status.OK);
      // update data
    } else { // userId does not match user
      response = Response.status(Response.Status.UNAUTHORIZED);
    }

    return response.build();
  }

  @DELETE
  @Path("/{userId}")
  public Response deleteUser(@PathParam("userId") long userId) {

    // authenticate user --> UNAUTHORIZED

    ResponseBuilder response;
    User user = dao.getUser(userId);
    if (user == null) {
      response = Response.status(Response.Status.BAD_REQUEST); // invalid user id
    }

    boolean success = dao.deleteUser(userId);
    if (success) {
      response = Response.status(Response.Status.OK); // user account successfully deleted
    } else {
      response = Response.status(Response.Status.BAD_REQUEST); // failure
    }

    return response.build();
  }

}
