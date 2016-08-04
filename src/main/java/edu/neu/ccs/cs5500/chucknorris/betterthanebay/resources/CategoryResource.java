package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Category;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.CategoryDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;

/**
 * Created by yoganandc on 7/22/16.
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api
public class CategoryResource {

    private final CategoryDAO dao;

    public CategoryResource(CategoryDAO dao) {
        this.dao = dao;
    }

    @GET
    @UnitOfWork
    @ApiOperation(
            value = "Returns a list of all existing categories",
            notes = "Returns a list of all existing categories",
            response = Category.class)
    public Response getAllCategories(@Auth User loggedInUser) {
        return Response.ok(dao.getAllCategories()).build();
    }
}
