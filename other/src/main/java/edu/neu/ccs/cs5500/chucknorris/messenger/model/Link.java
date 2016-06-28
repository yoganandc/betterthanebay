package edu.neu.ccs.cs5500.chucknorris.messenger.model;

/**
 * Created by yoganandc on 6/4/16.
 */
public class Link {

    private String rel;
    private String link;

    public Link() {
    }

    public Link(String link, String rel) {
        this.rel = rel;
        this.link = link;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
