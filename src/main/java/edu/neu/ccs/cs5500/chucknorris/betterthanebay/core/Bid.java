package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "bid")
@NamedQueries(value = {@NamedQuery(
    name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid.getCurrentWinningBid",
    query = "SELECT b FROM Bid b WHERE b.itemId = :item_id ORDER BY b.amount DESC"),
        @NamedQuery(name  = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid.getBidsForUser",
        query = "SELECT b FROM Bid b WHERE b.userId = :user_id ORDER BY b.time DESC"),
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid.getBidsForItem",
        query = "SELECT b FROM Bid b WHERE b.itemId = :item_id ORDER BY b.time DESC")
})
public class Bid {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(required = true)
  private Long id;

  @Column(name = "item_id", nullable = false)
  private Long itemId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(nullable = false)
  @NotNull
  @DecimalMin(value = "0.0")
  private BigDecimal amount;

  @Column(name = "created", nullable = false, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @ApiModelProperty(required = true)
  private Date time;

  @Column(name = "payment_id", nullable = false)
  @NotNull
  private Long paymentId;

  public Bid(Long id, Long itemId, Long userId, BigDecimal amount, Date time, Long paymentId) {
    this.id = id;
    this.itemId = itemId;
    this.userId = userId;
    this.amount = amount;
    this.time = time;
    this.paymentId = paymentId;
  }

  public Bid() {

  }

  public Bid(Bid obj) {
      if(obj.getId() != null) {
          this.id = new Long(obj.getId());
      }
    if (obj.getItemId() != null) {
      this.itemId = new Long(obj.getItemId());
    }
    if (obj.getUserId() != null) {
      this.userId = new Long(obj.getUserId());
    }
    this.amount = new BigDecimal(obj.getAmount().toString());
    this.time = new Date(obj.getTime().getTime());
    this.paymentId = new Long(obj.getPaymentId());
  }

    @JsonProperty
  public Long getId() {
    return this.id;
  }

    @JsonIgnore
  public void setId(Long id) {
    this.id = id;
  }

  @JsonIgnore
  public Long getItemId() {
    return this.itemId;
  }

  @JsonIgnore
  public void setItemId(Long itemId) {
    this.itemId = itemId;
  }

  @JsonIgnore
  public Long getUserId() {
    return this.userId;
  }

  @JsonIgnore
  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public BigDecimal getAmount() {
    return this.amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @JsonProperty
  public Date getTime() {
    return this.time;
  }

  @JsonIgnore
  public void setTime(Date time) {
    this.time = time;
  }

  public Long getPaymentId() {
    return this.paymentId;
  }

  public void setPaymentId(Long paymentId) {
    this.paymentId = paymentId;
  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(getAmount(), bid.getAmount()) &&
                Objects.equals(getPaymentId(), bid.getPaymentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount(), getPaymentId());
    }

    @Override
  public String toString() {
    return "Bid{" + "id=" + this.id + ", itemId=" + this.itemId + ", userId=" + this.userId
        + ", amount=" + this.amount + ", time=" + this.time + ", paymentId=" + this.paymentId + '}';
  }
}
