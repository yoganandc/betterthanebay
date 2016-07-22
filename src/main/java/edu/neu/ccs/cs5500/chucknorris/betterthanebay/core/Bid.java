package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bid")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    @NotNull
    private Long itemId;

    @Column(name = "user_id", nullable = false)
    @NotNull
    private Long userId;

    @Column(nullable = false)
    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
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
        this.id = new Long(obj.getId());
        this.itemId = new Long(obj.getItemId());
        this.userId = new Long(obj.getUserId());
        this.amount = new BigDecimal(obj.getAmount().toString());
        this.time = new Date(obj.getTime().getTime());
        this.paymentId = new Long(obj.getPaymentId());
    }

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


    public BigDecimal getAmount() {
        return this.amount;
    }


    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public Date getTime() {
        return this.time;
    }


    public void setTime(Date time) {
        this.time = time;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(getId(), bid.getId()) &&
                Objects.equals(getItemId(), bid.getItemId()) &&
                Objects.equals(getUserId(), bid.getUserId()) &&
                Objects.equals(getAmount(), bid.getAmount()) &&
                Objects.equals(getTime(), bid.getTime()) &&
                Objects.equals(getPaymentId(), bid.getPaymentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getItemId(), getUserId(), getAmount(), getTime(), getPaymentId());
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", time=" + time +
                ", paymentId=" + paymentId +
                '}';
    }
}
