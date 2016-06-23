package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import java.util.Date;

public class Feedback {

  private Long id;
  private String message;
  private Date created;
  private Date updated;
  private Integer rating;

  public String getMessage() {
    return this.message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Date getCreated() {
    return this.created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public Date getUpdated() {
    return this.updated;
  }

  public void setUpdated(Date updated) {
    this.updated = updated;
  }

  public Integer getRating() {
    return this.rating;
  }

  public void setRating(Integer rating) {
    this.rating = rating;
  }

}
