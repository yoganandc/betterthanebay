package com.example.helloworld.resources;

import java.util.List;

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

import com.example.helloworld.User;
import com.example.helloworld.UserDAO;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

  UserDAO userDAO;

  public UserResource(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  @GET
  public List<User> getAll() {
    return this.userDAO.getAll();
  }

  @GET
  @Path("/{id}")
  public User get(@PathParam("id") Long id) {
    return this.userDAO.findById(id);
  }

  @POST
  public User add(@Valid User user) {
    Long newId = this.userDAO.insert(user);
    return user.setId(newId);
  }

  @PUT
  @Path("/{id}")
  public User update(@PathParam("id") Long id, @Valid User user) {
    user = user.setId(id);
    this.userDAO.update(user);

    return user;
  }

  @DELETE
  @Path("/{id}")
  public void delete(@PathParam("id") Long id) {
    this.userDAO.deleteById(id);
  }


}
