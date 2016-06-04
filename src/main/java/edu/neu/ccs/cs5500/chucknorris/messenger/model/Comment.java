package edu.neu.ccs.cs5500.chucknorris.messenger.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Created by yoganandc on 6/2/16.
 */

@XmlRootElement
public class Comment {

    private long id;
    private long messageId;
    private String message;
    private Date updated;
    private String author;

    public Comment() {

    }

    public Comment(long id, long messageId, String message, String author) {
        this.id = id;
        this.messageId = messageId;
        this.message = message;
        this.author = author;
        this.updated = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
