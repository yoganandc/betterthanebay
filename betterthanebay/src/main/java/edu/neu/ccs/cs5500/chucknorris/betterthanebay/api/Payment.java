package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import java.util.Date;

public class Payment {

  private Long id;
  private Person person;
  private String number;
  private Date exp;
  private Address address;
  private Integer csv;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Person getPerson() {
    return this.person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public String getNumber() {
    return this.number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public Date getExp() {
    return this.exp;
  }

  public void setExp(Date exp) {
    this.exp = exp;
  }

  public Address getAddress() {
    return this.address;
  }

  public void setAddress(Address address) {
    this.address = address;
  }

  public Integer getCsv() {
    return this.csv;
  }

  public void setCsv(Integer csv) {
    this.csv = csv;
  }

}
