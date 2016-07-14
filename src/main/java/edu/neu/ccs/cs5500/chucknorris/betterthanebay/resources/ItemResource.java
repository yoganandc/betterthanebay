package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import javax.ws.rs.core.Response.ResponseBuilder;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Feedback;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import io.dropwizard.jersey.params.DateTimeParam;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;


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
    public Response getItem(@PathParam("itemId") LongParam itemId) {

        Item item = dao.findById(itemId.get());
        if (item == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(item).build();
    }

    @GET
    public Response getItems(@QueryParam("name") NonEmptyStringParam name,
                             @QueryParam("category") IntParam category, @QueryParam("dateFrom") DateTimeParam dateFrom,
                             @QueryParam("dateTo") DateTimeParam dateTo, @QueryParam("priceFrom") IntParam priceFrom,
                             @QueryParam("priceTo") IntParam priceTo, @QueryParam("start") IntParam start, @QueryParam("size") IntParam size) {
        if (name == null || !name.get().isPresent()) {
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
    public Response addItem(@Valid Item item) {

        Item createdItem = dao.create(item);

        if (createdItem == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // failure
        }
        return Response.created(URI.create("/items/" + createdItem.getId())).entity(createdItem).build();
    }

    @PUT
    @Path("/{itemId}")
    public Response updateItem(@PathParam("itemId") LongParam itemId, @Valid Item item) {

        // UNAUTHORIZED user

        if (dao.findById(itemId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Item updatedItem = dao.update(item);  // dao.updateItem(bidId, bid);

        if (updatedItem == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.ok(updatedItem).build();
    }

    @DELETE
    @Path("/{itemId}")
    public Response deleteItem(@PathParam("itemId") LongParam itemId) {

        // authenticate user

        if (dao.findById(itemId.get()) == null) {
            return Response.status(Response.Status.NOT_FOUND).build(); // item doesn't exist
        }

        boolean success = false; // dao.deleteItem(itemId);
        if (success) {
            return Response.status(Response.Status.NO_CONTENT).build(); // item successfully deleted
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // failure
        }
    }

    @Path("/{itemId}/bids")
    public BidResource getBidResource() {
        return new BidResource(bidDAO);
    }

    @Path("/{itemId}/feedback")
    public FeedbackResource getFeedbackResource() {
        return new FeedbackResource(feedbackDAO);
    }
}
