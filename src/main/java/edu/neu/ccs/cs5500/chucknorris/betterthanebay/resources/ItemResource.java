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
import io.dropwizard.jersey.params.DateTimeParam;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import org.joda.time.DateTime;


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
    public Response getItems(@QueryParam("name") NonEmptyStringParam name,
                             @QueryParam("category") IntParam category, @QueryParam("dateFrom") DateTimeParam dateFrom,
                             @QueryParam("dateTo") DateTimeParam dateTo, @QueryParam("priceFrom") IntParam priceFrom,
                             @QueryParam("priceTo") IntParam priceTo, @QueryParam("start") IntParam start, @QueryParam("size") IntParam size) {
        if (name == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        DateTime startDate;
        if (dateFrom == null) {
            startDate = new DateTime(0);
        } else {
            startDate = dateFrom.get();
        }

        DateTime endDate;
        if (dateTo == null) {
            endDate = new DateTime();
        } else {
            endDate = dateTo.get();
        }

        Double startPrice;
        if (priceFrom == null) {
            startPrice = 0.0;
        } else {
            startPrice = priceFrom.get().doubleValue();
        }

        Double endPrice;
        if (priceTo == null) {
            endPrice = Double.MAX_VALUE;
        } else {
            endPrice = priceTo.get().doubleValue();
        }

        int startVal;
        if (start == null) {
            startVal = 0;
        } else {
            startVal = start.get();
        }

        int sizeVal;
        if (size == null) {
            sizeVal = 20;
        } else {
            sizeVal = size.get();
        }

        List<Item> list = null;

        if (category != null) {
            //list = dao.searchWithCategory(name.get(), startDate, endDate, startPrice, endPrice, startVal, sizeVal, category.get());
        } else {
            //list = dao.searchWithoutCategory(name.get(), startDate, endDate, startPrice, endPrice, startVal, sizeVal);

        }

        if (list == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else if (list.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(list).build();
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

//    @Path("/{itemId}/bids")
//    public BidResource getBidResource() {
//        return new BidResource(new BidDAO())
//    }

}
