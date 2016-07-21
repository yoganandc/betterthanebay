package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.net.URI;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.DateTimeParam;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;


@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    private ItemDAO dao;
    private BidDAO bidDAO;
    private FeedbackDAO feedbackDAO;

    public ItemResource(ItemDAO dao, BidDAO bidDAO, FeedbackDAO feedbackDAO) {
        this.dao = dao;
        this.bidDAO = bidDAO;
        this.feedbackDAO = feedbackDAO;
    }

    @GET
    @Path("/{itemId}")
    @UnitOfWork
    @ApiOperation(
            value = "Find item with given id",
            notes = "If {itemId} exists, returns the corresponding item object",
            response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Item ID doesn't exist")})
    public Response getItem(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                            @Auth User loggedInUser) {

        Item item = this.dao.findById(itemId.get());
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(item).build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(
            value = "Find items by keyword, category, date range, and/or price range",
            notes = "Returns a list of items matching the given parameters for item name, category, start date, end date, lowest price, highest price, item offset number, and size of item list",
            response = Item.class,
            responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No matching results found"),
            @ApiResponse(code = 401, message = "User must be logged in")})
    public Response getItems(@ApiParam(value = "Item keyword", required = true) @QueryParam("name") NonEmptyStringParam name,
                             @ApiParam(value = "Item category", required = false) @QueryParam("category") IntParam category, @QueryParam("dateFrom") DateTimeParam dateFrom,
                             @ApiParam(value = "Start date of an auction", required = false) @QueryParam("dateTo") DateTimeParam dateTo, @QueryParam("priceFrom") IntParam priceFrom,
                             @ApiParam(value = "End date of an auction", required = false) @QueryParam("priceTo") IntParam priceTo,
                             @ApiParam(value = "Results offset", required = false) @QueryParam("start") IntParam start,
                             @ApiParam(value = "Results list size", required = false) @QueryParam("size") IntParam size, @Auth User loggedInUser) {
        if ((name == null) || !name.get().isPresent()) {
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
            //list = dao.searchWithCategory(name.get(), startDate, endDate, startPrice, endPrice,
            // startVal, sizeVal, category.get());
        } else {
            // list = dao.searchWithoutCategory(name.get(), startDate, endDate, startPrice, endPrice,
            // startVal, sizeVal);

        }

        if (list == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } else if (list.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(list).build();
    }


    @POST
    @UnitOfWork
    @ApiOperation(
            value = "Creates a new item auction",
            notes = "Adds the given item to the logged in user's auctions",
            response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid item data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in")})
    public Response addItem(@Valid Item item, @Auth User loggedInUser) {

        Item createdItem = this.dao.create(item);

        if (createdItem == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // failure
        }
        return Response.created(URI.create("/items/" + createdItem.getId())).entity(createdItem)
                .build();
    }

    @PUT
    @Path("/{itemId}")
    @UnitOfWork
    @ApiOperation(
            value = "Updates the item details",
            notes = "Updates the given item's auction details",
            response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid item data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "User cannot update item data for another user"),
            @ApiResponse(code = 404, message = "Item ID not found")})
    public Response updateItem(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                               @Valid Item item, @Auth User loggedInUser) {

        if (this.dao.findById(itemId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Item updatedItem = this.dao.update(item); // dao.updateItem(itemId, item);

        return Response.ok(updatedItem).build();
    }

    @DELETE
    @Path("/{itemId}")
    @UnitOfWork
    @ApiOperation(
            value = "Deletes item",
            notes = "Deletes the item with given item ID")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Item successfully deleted"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Item auction does not belong to signed in user"),
            @ApiResponse(code = 404, message = "Item ID not found")})
    public Response deleteItem(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                               @Auth User loggedInUser) {

        // authenticate user

        if (this.dao.findById(itemId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // item doesn't exist
        }

        boolean success = false; // dao.deleteItem(itemId);
        return Response.status(Response.Status.NO_CONTENT).build(); // item successfully deleted
    }

    @Path("/{itemId}/bids")
    public BidResource getBidResource() {
        return new BidResource(this.bidDAO, this.dao);
    }

    @Path("/{itemId}/feedback")
    public FeedbackResource getFeedbackResource() {
        return new FeedbackResource(this.feedbackDAO);
    }
}
