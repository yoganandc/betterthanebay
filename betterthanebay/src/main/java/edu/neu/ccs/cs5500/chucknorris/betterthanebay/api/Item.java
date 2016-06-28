package edu.neu.ccs.cs5500.chucknorris.betterthanebay.api;

import java.util.Date;
import java.util.List;

public class Item {

  private Long id;
  private String name;
  private String description;
  List<Integer> categories;
  private Double initialPrice;
  private Date startDate;
  private Date endDate;
  private String image;
  private Feedback buyerFeedback;
  private Feedback sellerFeedback;
  private Long bidId;
  private Long userId;
  private Date created;
  private Date updated;

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

public String getDescription() {
    return this.name;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Integer> getCategories() {
    return this.categories;
  }

  public void setCategories(List<Integer> categories) {
    this.categories = categories;
  }

  public Double getInitialPrice() {
    return this.initialPrice;
  }

  public void setInitialPrice(Double initialPrice) {
    this.initialPrice = initialPrice;
  }

  public Date getStartDate() {
    return this.startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return this.endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getImage() {
    return this.image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Feedback getBuyerFeedback() {
    return this.buyerFeedback;
  }

  public void setBuyerFeedback(Feedback buyerFeedback) {
    this.buyerFeedback = buyerFeedback;
  }

  public Feedback getSellerFeedback() {
    return this.sellerFeedback;
  }

  public void setSellerFeedback(Feedback sellerFeedback) {
    this.sellerFeedback = sellerFeedback;
  }

  public Long getBidId() {
    return this.bidId;
  }

  public void setBidId(Long bidId) {
    this.bidId = bidId;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

}
