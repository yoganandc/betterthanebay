package edu.neu.ccs.cs5500.chucknorris.messenger.resources;

/**
 * Created by yoganandc on 6/3/16.
 */

import edu.neu.ccs.cs5500.chucknorris.messenger.beans.CommentFilter;
import edu.neu.ccs.cs5500.chucknorris.messenger.model.Comment;
import edu.neu.ccs.cs5500.chucknorris.messenger.service.CommentService;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentResource {

    CommentService commentService = new CommentService();

    @GET
    public List<Comment> getComments(@BeanParam CommentFilter params,
                                    @PathParam("messageId") long messageId) {

        if(params.getStart() != null && params.getStart() >= 0) {
            if(params.getSize() != null && params.getSize() >= 1) {
                return commentService.getCommentsByPage(messageId, params.getStart(), params.getSize());
            }
        }

        return commentService.getAllComments(messageId);
    }

    @GET
    @Path("/{commentId}")
    public Comment getComment(@PathParam("messageId") long messageId,
                              @PathParam("commentId") long id) {

        return commentService.getComment(messageId, id);
    }

    @POST
    public Response addComment(@PathParam("messageId") long messageId,
                               Comment comment,
                               @Context UriInfo uriInfo) {

        // THIS FUNCTION RETURNS A 201 CREATED WITH THE LOCATION HEADER SET TO NEW URL
        Comment addComment = commentService.addComment(messageId, comment);
        String addId = String.valueOf(addComment.getId());
        URI addUri = uriInfo.getAbsolutePathBuilder().path(addId).build();
        return Response.created(addUri).entity(addComment).build();
    }

    @PUT
    @Path("/{commentId}")
    public Comment updateComment(@PathParam("messageId") long messageId,
                                 @PathParam("commentId") long id, Comment comment) {
        comment.setId(id);
        comment.setMessageId(messageId);
        return commentService.updateComment(messageId, comment);
    }

    @DELETE
    @Path("/{commentId}")
    public void deleteComment(@PathParam("messageId") long messageId,
                              @PathParam("commentId") long commentId) {

        commentService.deleteComment(messageId, commentId);
    }

}
