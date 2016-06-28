package edu.neu.ccs.cs5500.chucknorris.messenger.beans;

import edu.neu.ccs.cs5500.chucknorris.messenger.model.Comment;

import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * Created by yoganandc on 6/3/16.
 */
public class CommentFilter {

    private @QueryParam("start") Integer start;
    private @QueryParam("size") Integer size;

    public CommentFilter() {}

    public Integer getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
