package edu.neu.ccs.cs5500.chucknorris.betterthanebay.api;

import java.util.Date;

public class Person {

  private Long id;
  private String first;
  private String middle;
  private String last;
  private Date birthday;
  private Date created;
  private Date updated;

 
  public String getFirst() {
    return this.first;
  }

  
  public void setFirst(String first) {
    this.first = first;
  }

 
  public String getMiddle() {
    return this.middle;
  }

  public void setMiddle(String middle) {
    this.middle = middle;
  }

  public String getLast() {
    return this.last;
  }

  public void setLast(String last) {
    this.last = last;
  }

}
