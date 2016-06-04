package edu.neu.ccs.cs5500.chucknorris.messenger.service;

import edu.neu.ccs.cs5500.chucknorris.messenger.database.MessengerDB;
import edu.neu.ccs.cs5500.chucknorris.messenger.model.Comment;
import edu.neu.ccs.cs5500.chucknorris.messenger.model.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yoganandc on 6/2/16.
 */
public class CommentService {

    private Map<Long, Comment> comments = MessengerDB.getComments();
    private Map<Long, Message> messages = MessengerDB.getMessages();

    public List<Comment> getAllComments(long messageId) {
        List<Comment> ret = new ArrayList<>();
        for(Comment c : comments.values()) {
            if(c.getMessageId() == messageId) {
                ret.add(c);
            }
        }
        return ret;
    }

    public List<Comment> getCommentsByPage(long messageId, int start, int size) {
        List<Comment> messageComments = this.getAllComments(messageId);
        int numComments = messageComments.size();

        if(start >= numComments) {
            return new ArrayList<>();
        }
        else if(start + size > numComments) {
            return messageComments.subList(start, numComments);
        }
        else {
            return messageComments.subList(start, start + size);
        }
    }

    public Comment getComment(long messageId, long id) {
        Comment ret = comments.get(id);
        if(ret == null || ret.getMessageId() != messageId) {
            return null;
        }
        return ret;
    }

    public Comment addComment(long messageId, Comment comment) {
        if(messages.containsKey(messageId)) {
            comment.setId(MessengerDB.getNextCommentId());
            comment.setUpdated(new Date());
            comments.put(comment.getId(), comment);
            return comment;
        }
        return null;
    }

    public Comment updateComment(long messageId, Comment comment) {
        if(comments.containsKey(comment.getId())) {
            if(comments.get(comment.getId()).getMessageId() == messageId) {
                comment.setUpdated(new Date());
                comments.put(comment.getId(), comment);
                return comment;
            }
        }
        return null;
    }

    public Comment deleteComment(long messageId, long id) {
        if(comments.containsKey(id)) {
            if (comments.get(id).getMessageId() == messageId) {
                return comments.remove(id);
            }
        }
        return null;
    }

}
