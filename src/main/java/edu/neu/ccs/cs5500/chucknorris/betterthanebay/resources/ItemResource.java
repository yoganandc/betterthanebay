package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Date;
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

import org.joda.time.DateTime;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
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
    @ApiOperation(value = "Find item with given id",
            notes = "If {itemId} exists, returns the corresponding item object", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Item ID doesn't exist")})
    public Response getItem(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @Auth User loggedInUser) {

        /* TODO */
        // RETURN A NOT FOUND IF USER IS ACCESSING A NON-ACTIVE ITEM
        // AND IS NOT THE USER WHO POSTED IT

        Item item = this.dao.findById(itemId.get());
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(item).build();
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Find items by keyword, category, date range, and/or price range",
            notes = "Returns a list of items matching the given parameters for item name, category, "
                    + "lowest price, highest price, item offset number, and size of item list",
            response = Item.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No matching results found"),
            @ApiResponse(code = 401, message = "User must be logged in")})
    public Response getItems(
            @ApiParam(value = "Item keyword",
                    required = false) @QueryParam("name") NonEmptyStringParam name,
            @ApiParam(value = "Item category",
                    required = false) @QueryParam("category") LongParam category,
            @ApiParam(value = "Lowest price of item",
                    required = false) @QueryParam("priceFrom") IntParam priceFrom,
            @ApiParam(value = "Highest price of item",
                    required = false) @QueryParam("priceTo") IntParam priceTo,
            @ApiParam(value = "Results offset", required = false) @QueryParam("start") IntParam start,
            @ApiParam(value = "Results list size", required = false) @QueryParam("size") IntParam size,
            @Auth User loggedInUser) {

        String searchQuery;
        if (name == null || !name.get().isPresent()) {
            searchQuery = "";
        }
        else {
            searchQuery = name.get().get();
        }

        BigDecimal startPrice;
        if (priceFrom == null) {
            startPrice = new BigDecimal("0");
        } else {
            startPrice = new BigDecimal(priceFrom.get());
        }

        /* TODO */
        //CHECK ENDPRICE > STARTPRICE

        BigDecimal endPrice;
        if (priceTo == null) {
            endPrice = new BigDecimal(Integer.MAX_VALUE);
        } else {
            endPrice = new BigDecimal(priceTo.get());
        }

        /* TODO */
        //CHECK SIZE, START > 0

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
            list = dao.searchWithCategory(searchQuery, startPrice, endPrice, category.get(), startVal, sizeVal);
        } else {
            list = dao.searchWithoutCategory(searchQuery, startPrice, endPrice, startVal, sizeVal);
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
    @ApiOperation(value = "Creates a new item auction",
            notes = "Adds the given item to the logged in user's auctions", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid item data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in")})
    public Response addItem(@Valid Item item, @Auth User loggedInUser) {

        /* TODO */
        //VALIDATE START_DATE IS IN FUTURE
        // AND END_DATE > START_DATE

        //null out item id
        item.setId(null);

        //set json ignored properties
        Date now = new Date();
        item.setCreated(now);
        item.setUpdated(now);
        item.setUserId(loggedInUser.getId());

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
    @ApiOperation(value = "Updates the item details",
            notes = "Updates the given item's auction details", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid item data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "User cannot update item data for another user"),
            @ApiResponse(code = 404, message = "Item ID not found")})
    public Response updateItem(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @Valid Item item, @Auth User loggedInUser) {

        //bad request if entity's id does not match path id
        if(!item.getId().equals(itemId.get())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        Item found = dao.findById(itemId.get());
        if(found == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // forbidden to update if logged in user did not post item
        if(!found.getUserId().equals(loggedInUser.getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        //if item's end_date has crossed, you can no longer modify the item
        if(!found.getEndDate().after(new Date())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        //if item's start_date has crossed, you can no longer modify name, start_date, and initialprice
        if(found.getStartDate().after(new Date())) {
            item.setStartDate(found.getStartDate());
            item.setName(found.getName());
            item.setInitialPrice(found.getInitialPrice());
        }

        /* TODO */
        //VALIDATE START_DATE IS IN FUTURE
        // AND END_DATE > START_DATE

        //set json ignored properties
        item.setUserId(loggedInUser.getId());
        item.setCreated(found.getCreated());
        item.setUpdated(new Date());

        Item updatedItem = this.dao.update(item);

        return Response.ok(updatedItem).build();
    }

    @DELETE
    @Path("/{itemId}")
    @UnitOfWork
    @ApiOperation(value = "Deletes item", notes = "Deletes the item with given item ID")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "Item successfully deleted"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 403, message = "Item auction does not belong to signed in user"),
            @ApiResponse(code = 404, message = "Item ID not found")})
    public Response deleteItem(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @Auth User loggedInUser) {

        Item found = dao.findById(itemId.get());

        if (found == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // item doesn't exist
        }

        if(!found.getUserId().equals(loggedInUser.getId())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        boolean success = this.dao.deleteItem(itemId.get());
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
