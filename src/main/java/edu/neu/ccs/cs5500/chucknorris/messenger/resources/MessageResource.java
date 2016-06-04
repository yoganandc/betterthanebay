package edu.neu.ccs.cs5500.chucknorris.messenger.resources;

/**
 * Created by yoganandc on 5/26/16.
 */

import edu.neu.ccs.cs5500.chucknorris.messenger.beans.MessageFilter;
import edu.neu.ccs.cs5500.chucknorris.messenger.service.MessageService;
import edu.neu.ccs.cs5500.chucknorris.messenger.model.Message;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {

    MessageService messageService = new MessageService();

    @GET
    public List<Message> getMessages(@BeanParam MessageFilter params) {
        if(params.getAuthor() != null && !params.getAuthor().isEmpty()) {
            return messageService.getMessagesByUser(params.getAuthor());
        }
        if(params.getStart() != null && params.getSize() >= 0 &&
                params.getSize() != null && params.getSize() >= 1) {
            return messageService.getMessagesByPage(params.getStart(), params.getSize());
        }
        return messageService.getAllMessages();
    }

    @GET
    @Path("/{messageId}")
    public Message getMessage(@PathParam("messageId") long messageId) {
        return messageService.getMessage(messageId);
    }

    @POST
    public Message addMessage(Message message) {
        return messageService.addMessage(message);
    }

    @PUT
    @Path("/{messageId}")
    public Message updateMessage(@PathParam("messageId") long messageId, Message message) {
        message.setId(messageId);
        return messageService.updateMessage(message);
    }

    @DELETE
    @Path("/{messageId}")
    public void removeMessage(@PathParam("messageId") long messageId) {
        messageService.removeMessage(messageId);
    }

    @Path("/{messageId}/comments")
    public CommentResource getCommentResource() {
        return new CommentResource();
    }
}
