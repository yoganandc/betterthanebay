package edu.neu.ccs.cs5500.chucknorris.betterthanebay.api;

public class Address {

  private Long id;
  private String line1;
  private String line2;
  private String city;
  private Integer state; // enums
  private String zip;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLine1() {
    return this.line1;
  }

  public void setLine1(String line1) {
    this.line1 = line1;
  }

  public String getLine2() {
    return this.line2;
  }

  public void setLine2(String line2) {
    this.line2 = line2;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Integer getState() {
    return this.state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public String getZip() {
    return this.zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

}
