package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.CreatedTimestamp;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UpdatedTimestamp;

@Entity
@Table(name = "item")
@NamedQueries(value = {
    @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsForUser",
                query = "SELECT i FROM Item i WHERE i.userId = :user_id")
})
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  private String description;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.PERSIST},
      fetch = FetchType.EAGER)
  @JoinTable(name = "categories", joinColumns = @JoinColumn(name = "item_id", nullable = false) ,
      inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false) )
  private Set<Category> categories;

  @Column(name = "price", nullable = false)
  private BigDecimal initialPrice;

  @Column(name = "start_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDate;

  @Column(name = "end_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDate;

  private String image;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "buyer_feedback_id")
  private Feedback buyerFeedback;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "seller_feedback_id")
  private Feedback sellerFeedback;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "winning_bid_id")
  private Bid bid;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreatedTimestamp
  private Date created;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @UpdatedTimestamp
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

  public Set<Category> getCategories() {
    return this.categories;
  }

  public void setCategories(Set<Category> categories) {
    this.categories = categories;
  }

  public BigDecimal getInitialPrice() {
    return this.initialPrice;
  }

  public void setInitialPrice(BigDecimal initialPrice) {
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

  public Bid getBid() {
    return this.bid;
  }

  public void setBid(Bid bid) {
    this.bid = bid;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    Item item = (Item) o;
    return Objects.equals(getId(), item.getId()) && Objects.equals(getName(), item.getName())
        && Objects.equals(getDescription(), item.getDescription())
        && Objects.equals(getCategories(), item.getCategories())
        && Objects.equals(getInitialPrice(), item.getInitialPrice())
        && Objects.equals(getStartDate(), item.getStartDate())
        && Objects.equals(getEndDate(), item.getEndDate())
        && Objects.equals(getImage(), item.getImage())
        && Objects.equals(getBuyerFeedback(), item.getBuyerFeedback())
        && Objects.equals(getSellerFeedback(), item.getSellerFeedback())
        && Objects.equals(getBid(), item.getBid()) && Objects.equals(getUserId(), item.getUserId())
        && Objects.equals(getCreated(), item.getCreated())
        && Objects.equals(getUpdated(), item.getUpdated());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getName(), getDescription(), getCategories(), getInitialPrice(),
        getStartDate(), getEndDate(), getImage(), getBuyerFeedback(), getSellerFeedback(), getBid(),
        getUserId(), getCreated(), getUpdated());
  }

  @Override
  public String toString() {
    return "Item{" + "id=" + this.id + ", name='" + this.name + '\'' + ", description='"
        + this.description + '\'' + ", categories=" + this.categories + ", initialPrice="
        + this.initialPrice + ", startDate=" + this.startDate + ", endDate=" + this.endDate
        + ", image='" + this.image + '\'' + ", buyerFeedback=" + this.buyerFeedback
        + ", sellerFeedback=" + this.sellerFeedback + ", bid=" + this.bid + ", userId="
        + this.userId + ", created=" + this.created + ", updated=" + this.updated + '}';
  }
}
