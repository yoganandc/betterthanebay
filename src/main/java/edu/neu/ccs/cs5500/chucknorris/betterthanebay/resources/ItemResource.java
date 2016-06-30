package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.util.Date;
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

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;


@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
public class ItemResource {

   private ItemDAO dao;

   public ItemResource(ItemDAO dao) {
   super();
   this.dao = dao;
   }

  // all items
  @GET
  public List<Item> getItems() {

    return null; //dao.getAllItems();
  }

  @GET
  @Path("/{itemId}")
  public Item getItem(@PathParam("itemId") long itemId) {

    return null; //dao.getItem(itemId);
  }

  @GET
  @Path("/users/{userId}/items")
  public Item getUserItems(@PathParam("userId") long userId) {
    // validate user id
    return null; //dao.getUserItems(userId);

  }


  @GET
  public List<Item> getItems(@QueryParam("name") String name,
      @QueryParam("category") String category, @QueryParam("dateFrom") Date dateFrom,
      @QueryParam("dateTo") Date dateTo, @QueryParam("priceFrom") Double priceFrom,
      @QueryParam("priceTo") Double priceTo) {


    return null;


  }


  @POST
  public Response addItem(Item item) {
    ResponseBuilder response;

    if ((item.getName() == null) || (item.getDescription() == null)
        || (item.getInitialPrice() == null) || (item.getStartDate() == null)
        || (item.getEndDate() == null)) {
      response = Response.status(Response.Status.BAD_REQUEST);

    } else {
      Item createdBid = null; //dao.createItem(item);
      if (createdBid == null) {
        response = Response.status(Response.Status.BAD_REQUEST); // failure
        //
      } else {
        response = Response.status(Response.Status.CREATED);
        // response -> add item data
      }
    }

    return response.build();

  }

  @PUT
  @Path("/{itemId}")
  public Response updateItem(@PathParam("itemId") long itemId, Item item) {
    ResponseBuilder response;
    // UNAUTHORIZED user

    // if item id exists ---------------

    // update data
    response = Response.status(Response.Status.OK); // successfully updated
    // else
    response = Response.status(Response.Status.BAD_REQUEST); // failure || invalid data || not found

    return response.build();
  }

  @DELETE
  @Path("/{itemId}")
  public Response deleteItem(@PathParam("itemId") long itemId) {

    ResponseBuilder response;

    // authenticate user

    Item item = null; //dao.getItem(itemId);
    if (item == null) {
      response = Response.status(Response.Status.BAD_REQUEST); // invalid bid id
    }

    boolean success = false; // dao.deleteItem(itemId);
    if (success) {
      response = Response.status(Response.Status.OK); // item successfully deleted
    } else {
      response = Response.status(Response.Status.BAD_REQUEST); // failure
    }

    return response.build();
  }

}
