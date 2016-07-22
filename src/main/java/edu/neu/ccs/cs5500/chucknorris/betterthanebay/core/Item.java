package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;

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
import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "item")
@NamedQueries(value = {
    @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsForUser",
        query = "SELECT i FROM Item i WHERE i.userId = :user_id"),
    @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.findById",
        query = "SELECT i FROM Item i WHERE i.id = :id"),
    @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getActiveItems",
        query = "SELECT i FROM Item i WHERE i.userId = :user_id"
            + " AND i.endDate > CURRENT_TIMESTAMP "),
    @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.deleteItem",
        query = "DELETE FROM Item i WHERE i.id = :id")})
public class Item {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  @NotBlank
  private String name;

  private String description;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.PERSIST},
      fetch = FetchType.EAGER)
  @JoinTable(name = "categories", joinColumns = @JoinColumn(name = "item_id", nullable = false) ,
      inverseJoinColumns = @JoinColumn(name = "category_id", nullable = false) )
  @NotEmpty
  @Valid
  @OrderBy(clause = "id ASC")
  private SortedSet<Category> categories = new TreeSet<>();

  @Column(name = "price", nullable = false)
  @DecimalMin(value = "0.0")
  @NotNull
  private BigDecimal initialPrice;

  @Column(name = "start_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @NotNull
  private Date startDate;

  @Column(name = "end_date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @NotNull
  private Date endDate;

  private String image;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "buyer_feedback_id")
  @Valid
  private Feedback buyerFeedback;

  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "seller_feedback_id")
  @Valid
  private Feedback sellerFeedback;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "winning_bid_id")
  @Valid
  private Bid bid;

  @Column(name = "user_id", nullable = false)
  @NotNull
  private Long userId;

  @Column(nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  @Column(nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date updated;

  public Item() {

  }

  public Item(Long id, String name, String description, SortedSet<Category> categories,
      BigDecimal initialPrice, Date startDate, Date endDate, String image, Feedback buyerFeedback,
      Feedback sellerFeedback, Bid bid, Long userId, Date created, Date updated) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.categories = categories;
    this.initialPrice = initialPrice;
    this.startDate = startDate;
    this.endDate = endDate;
    this.image = image;
    this.buyerFeedback = buyerFeedback;
    this.sellerFeedback = sellerFeedback;
    this.bid = bid;
    this.userId = userId;
    this.created = created;
    this.updated = updated;
  }

  public Item(Item obj) {
    this.id = new Long(obj.getId());
    this.name = new String(obj.getName());
    this.description = new String(obj.getDescription());
    for (Category category : obj.getCategories()) {
      this.categories.add(new Category(category));
    }
    this.initialPrice = new BigDecimal(obj.getInitialPrice().toString());
    this.startDate = new Date(obj.getStartDate().getTime());
    this.endDate = new Date(obj.getEndDate().getTime());
    this.image = new String(obj.getImage());
    this.buyerFeedback = new Feedback(obj.getBuyerFeedback());
    this.sellerFeedback = new Feedback(obj.getSellerFeedback());
    this.bid = new Bid(obj.getBid());
    this.userId = new Long(obj.getUserId());
    this.created = new Date(obj.getCreated().getTime());
    this.updated = new Date(obj.getUpdated().getTime());
  }

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

  public SortedSet<Category> getCategories() {
    return this.categories;
  }

  public void setCategories(SortedSet<Category> categories) {
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
