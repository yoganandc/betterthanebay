package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import java.util.ArrayList;
import java.util.List;

public class User {

  private Long id;
  private String userName;
  private String password;
  private Person name;
  private List<Address> addresses;
  private List<Payment> payments;
  private Double rating;


  public Long getId() {
    return this.id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public String getUserName() {
    return this.userName;
  }


  public void setUserName(String userName) {
    this.userName = userName;
  }


  public String getPassword() {
    return this.password;
  }


  public void setPassword(String password) {
    this.password = password;
  }


  public Person getName() {
    return this.name;
  }


  public void setName(Person name) {
    this.name = name;
  }


  public List<Address> getAddresses() {
    return this.addresses;
  }


  public void setAddresses(List<Address> addresses) {
    this.addresses = addresses;
  }


  public List<Payment> getPayments() {
    return this.payments;
  }


  public void setPayments(List<Payment> payments) {
    this.payments = payments;
  }


  public Double getRating() {
    return this.rating;
  }


  public void setRating(Double rating) {
    this.rating = rating;
  }

}
