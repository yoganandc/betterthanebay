package com.example.helloworld.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.example.helloworld.User;
import com.example.helloworld.UserDAO;
import com.google.common.base.Optional;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.LongParam;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
  /**
   * The DAO object to manipulate users.
   */
  private UserDAO userDAO;

  /**
   * Constructor.
   *
   * @param userDAO DAO object to manipulate users.
   */
  public UserResource(UserDAO userDAO) {
    this.userDAO = userDAO;
  }

  /**
   * Looks for users whose first or last name contains the passed parameter as a substring. If name
   * argument was not passed, returns all users stored in the database.
   *
   * @param name query parameter
   * @return list of users whose first or last name contains the passed parameter as a substring or
   *         list of all employees stored in the database.
   */
  @GET
  @UnitOfWork
  public List<User> findByName(@QueryParam("name") Optional<String> name) {
    if (name.isPresent()) {
      return this.userDAO.findByName(name.get());
    } else {
      return this.userDAO.findAll();
    }
  }

  /**
   * Method looks for a user by her id.
   *
   * @param id the id of an user we are looking for.
   * @return Optional containing the found user or an empty Optional otherwise.
   */
  @GET
  @Path("/{id}")
  @UnitOfWork
  public Optional<User> findById(@PathParam("id") LongParam id) {
    return this.userDAO.findById(id.get());
  }


}
