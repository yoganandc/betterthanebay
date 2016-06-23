package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import java.util.Date;

public class Person {

  private Long id;
  private String first;
  private String middle;
  private String last;
  private Date birthday;
  private Date created;
  private Date updated;

  /**
   *
   * @return
   */
  public String getFirst() {
    return this.first;
  }

  /**
   *
   * @param first
   */
  public void setFirst(String first) {
    this.first = first;
  }

  /**
   *
   * @return
   */
  public String getMiddle() {
    return this.middle;
  }

  /**
   *
   * @param middle
   */
  public void setMiddle(String middle) {
    this.middle = middle;
  }

  /**
   *
   * @return the last name
   */
  public String getLast() {
    return this.last;
  }

  /**
   * Sets the last name of the user to given last
   *
   * @param last The last name
   */
  public void setLast(String last) {
    this.last = last;
  }

}
