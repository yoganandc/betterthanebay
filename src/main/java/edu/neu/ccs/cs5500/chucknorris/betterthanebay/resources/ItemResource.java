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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Api(value = "item")
public class ItemResource {


    private ItemDAO dao;
    private BidDAO bidDAO;
    private FeedbackResource feedbackResource;
    private BidResource bidResource;

    public ItemResource(ItemDAO dao, BidDAO bidDAO, FeedbackResource feedbackResource, BidResource bidResource) {
        this.dao = dao;
        this.bidDAO = bidDAO;
        this.feedbackResource = feedbackResource;
        this.bidResource = bidResource;
    }

    @GET
    @Path("/{itemId}")
    @UnitOfWork
    @ApiOperation(value = "Find item with given id",
            notes = "If {itemId} exists, returns the corresponding item object", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Item ID not found")})
    public Item getItem(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        Item item = this.dao.findById(itemId.get());

        if (item == null) {
            throw new WebApplicationException("Item not found", Response.Status.NOT_FOUND);
        }

        Date now = new Date();
        if ((item.getStartDate().after(now) || item.getEndDate().before(now)) && !loggedInUser.getId().equals(item.getUserId())) {
            throw new WebApplicationException("Item not found", Response.Status.NOT_FOUND);
        }

        return item;
    }

    @GET
    @UnitOfWork
    @ApiOperation(value = "Find items by keyword, category, date range, and/or price range",
            notes = "Returns a list of items matching the given parameters for item name, category, "
                    + "lowest price, highest price, item offset number, and size of item list",
            response = Item.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 204, message = "No matching results found"),
            @ApiResponse(code = 401, message = "User must be logged in"),
            @ApiResponse(code = 500, message = "Database error")})
    public List<Item> getItems(
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
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        String searchQuery;

        // NonEmptyStringParam#get() returns an Optional<String> where isPresent will return
        // false for both null and empty string, i.e., ""
        if ((name == null) || !name.get().isPresent()) {
            searchQuery = "";
        } else {
            searchQuery = name.get().get();
        }

        BigDecimal startPrice;
        if (priceFrom == null) {
            startPrice = new BigDecimal("0");
        } else {
            startPrice = new BigDecimal(priceFrom.get());
        }

        BigDecimal endPrice;
        if (priceTo == null) {
            endPrice = new BigDecimal(Integer.MAX_VALUE);
        } else {
            endPrice = new BigDecimal(priceTo.get());
        }

        if (endPrice.compareTo(startPrice) < 0) {
            throw new WebApplicationException("Highest price is less than lowest price", Response.Status.BAD_REQUEST);
        }

        int startVal;
        if (start == null) {
            startVal = 0;
        } else {
            if (start.get().compareTo(0) < 0) {
                throw new WebApplicationException("List offset must be greater than 0", Response.Status.BAD_REQUEST);
            }
            startVal = start.get();
        }

        int sizeVal;
        if (size == null) {
            sizeVal = 20;
        } else {
            if (size.get().compareTo(0) < 0) {
                throw new WebApplicationException("List size must be greater than 0", Response.Status.BAD_REQUEST);
            }
            sizeVal = size.get();
        }

        List<Item> list = null;

        if (category != null) {
            list = this.dao.searchWithCategory(searchQuery, startPrice, endPrice, category.get(),
                    startVal, sizeVal);
        } else {
            list = this.dao.searchWithoutCategory(searchQuery, startPrice, endPrice, startVal, sizeVal);
        }

        if (list.isEmpty()) {
            throw new WebApplicationException("No items found", Response.Status.NOT_FOUND);
        }
        return list;
    }


    @POST
    @UnitOfWork
    @ApiOperation(value = "Creates a new item auction",
            notes = "Adds the given item to the logged in user's auctions", response = Item.class)
    @ApiResponses(value = {@ApiResponse(code = 400, message = "Invalid item data supplied"),
            @ApiResponse(code = 401, message = "User must be signed in"),
            @ApiResponse(code = 500, message = "Database error")})
    public Response addItem(@Valid Item item, @ApiParam(hidden = true) @Auth User loggedInUser, @Context UriInfo uriInfo) {

        Date now = new Date();
        if (item.getStartDate().before(now)) {
            throw new WebApplicationException("Auction start date/time precedes current date/time", Response.Status.BAD_REQUEST);
        }

        if (item.getEndDate().before(item.getStartDate())) {
            throw new WebApplicationException("Auction end date/time precedes start date/time", Response.Status.BAD_REQUEST);
        }

        // null out item id
        item.setId(null);

        // start date could be null
        if(item.getStartDate() == null) {
            item.setStartDate(now);
        }

        // set json ignored properties
        item.setCreated(now);
        item.setUpdated(now);
        item.setUserId(loggedInUser.getId());

        Item createdItem = this.dao.create(item);

        URI uri = uriInfo.getAbsolutePathBuilder().path(createdItem.getId().toString()).build();
        return Response.created(uri).entity(createdItem).build();
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
    public Item updateItem(@ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
                               @Valid Item item, @ApiParam(hidden = true) @Auth User loggedInUser) {

        Item found = this.dao.findById(itemId.get());
        if (found == null) {
            throw new WebApplicationException("Item not found", Response.Status.NOT_FOUND);
        }

        // forbidden to update if logged in user did not post item
        if (!found.getUserId().equals(loggedInUser.getId())) {
            throw new WebApplicationException("Logged in user cannot modify another user's item", Response.Status.FORBIDDEN);
        }

        Date now = new Date();

        // if item's end_date has crossed, you can no longer modify the item
        if (!found.getEndDate().after(now)) {
            throw new WebApplicationException("Item auction has ended", Response.Status.FORBIDDEN);
        }

        // start date could be null
        if(item.getStartDate() == null) {
            item.setStartDate(found.getStartDate());
        }

        // if item's start_date has crossed, you can no longer modify name, start_date, and initial price
        if (found.getStartDate().before(now)) {
            item.setStartDate(found.getStartDate());
            item.setName(found.getName());
            item.setInitialPrice(found.getInitialPrice());
        }
        else if (item.getStartDate().before(now)) {
            throw new WebApplicationException("Auction start date/time precedes current date/time", Response.Status.BAD_REQUEST);
        }

        if (item.getEndDate().before(item.getStartDate())) {
            throw new WebApplicationException("Auction end date/time precedes start date/time", Response.Status.BAD_REQUEST);
        }

        // set json ignored properties
        item.setId(found.getId());
        item.setUserId(loggedInUser.getId());
        item.setCreated(found.getCreated());
        item.setUpdated(now);

        Item updatedItem = this.dao.update(item);

        return updatedItem;
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
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        Item found = this.dao.findById(itemId.get());

        // item doesn't exist
        if (found == null) {
            throw new WebApplicationException("Item not found", Response.Status.NOT_FOUND);
        }

        if (!found.getUserId().equals(loggedInUser.getId())) {
            throw new WebApplicationException("Logged in user cannot delete another user's item", Response.Status.FORBIDDEN);
        }

        boolean success = this.dao.deleteItem(itemId.get());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @GET
    @Path("/{itemId}/winning")
    @UnitOfWork
    @ApiOperation(value = "Get winning bid",
            notes = "Get winning bid for item, if bids exist",
            response = Bid.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "No bids found")})
    public Bid getCurrentWinningBid(
            @ApiParam(value = "Item ID", required = true) @PathParam("itemId") LongParam itemId,
            @ApiParam(hidden = true) @Auth User loggedInUser) {

        Bid bid = this.bidDAO.getCurrentWinningBid(itemId.get());

        if (bid == null) {
            throw new WebApplicationException("No bids have been placed", Response.Status.NOT_FOUND);
        }

        return bid;
    }

    @Path("/{itemId}/bids")
    public BidResource getBidResource() {
        return this.bidResource;
    }

    @Path("/{itemId}/feedback")
    public FeedbackResource getFeedbackResource() {
        return this.feedbackResource;
    }
}
