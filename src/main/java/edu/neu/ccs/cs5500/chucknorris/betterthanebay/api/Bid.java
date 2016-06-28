package edu.neu.ccs.cs5500.chucknorris.betterthanebay.api;

import java.util.Date;

public class Bid {

  private Long id;
  private Long itemId;
  private Long userId;
  private Double amount; /// 4.38293
  private Date time;
  private Long paymentId;

  public Long getId() {
    return this.id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public Long getItemId() {
    return this.itemId;
  }


  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }


  public Long getUserId() {
    return this.userId;
  }


  public void setUserId(Long userId) {
    this.userId = userId;
  }


  public Double getAmount() {
    return this.amount;
  }


  public void setAmount(Double amount) {
    this.amount = amount;
  }


  public Date getTime() {
    return this.time;
  }


  public void setTime(Date time) {
    this.time = time;
  }


  public Long getPaymentId() {
    return this.paymentId;
  }


  public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
  }

}
