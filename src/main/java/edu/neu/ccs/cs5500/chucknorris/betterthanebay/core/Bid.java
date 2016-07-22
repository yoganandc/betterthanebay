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

@Entity
@Table(name = "bid")
public class Bid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @OneToOne(optional = false)
    private Payment payment;

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


    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
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
                Objects.equals(getPayment(), bid.getPayment());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getItemId(), getUserId(), getAmount(), getTime(), getPayment());
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", amount=" + amount +
                ", time=" + time +
                ", payment=" + payment +
                '}';
    }
}
