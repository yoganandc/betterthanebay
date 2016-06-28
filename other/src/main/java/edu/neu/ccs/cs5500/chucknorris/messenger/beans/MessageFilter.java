package edu.neu.ccs.cs5500.chucknorris.messenger.beans;

import javax.ws.rs.QueryParam;

/**
 * Created by yoganandc on 6/2/16.
 */
public class MessageFilter {

    private @QueryParam("author") String author;
    private @QueryParam("start") Integer start;
    private @QueryParam("size") Integer size;

    public MessageFilter() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
