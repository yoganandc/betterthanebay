package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

@MappedSuperclass
public class Feedback {

    public static final String SELLER = "seller";
    public static final String BUYER = "buyer";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(required = true)
    private Long id;

    private String message;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(required = true)
    private Date created;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(required = true)
    private Date updated;

    @Column(name = "item_id", nullable = false)
    private Long itemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    @NotNull
    @Min(value = 0)
    @Max(value = 5)
    private Integer rating;

    public Feedback() {
    }

    public Feedback(Long id, String message, Date created, Date updated, Integer rating, Long itemId, Long userId) {
        this.id = id;
        this.message = message;
        this.created = created;
        this.updated = updated;
        this.rating = rating;
        this.itemId = itemId;
        this.userId = userId;
    }

    public Feedback(Feedback obj) {
        if(obj.getId() != null) {
            this.id = new Long(obj.getId());
        }
        if(obj.getMessage() != null) {
            this.message = new String(obj.getMessage());
        }
        this.created = new Date(obj.getCreated().getTime());
        this.updated = new Date(obj.getUpdated().getTime());
        this.rating = new Integer(obj.getRating());
        if(obj.getItemId() != null) {
            this.itemId = new Long(obj.getItemId());
        }
        if(obj.getUserId() != null) {
            this.userId = new Long(obj.getUserId());
        }
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty
    public Date getCreated() {
        return this.created;
    }

    @JsonIgnore
    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonProperty
    public Date getUpdated() {
        return this.updated;
    }

    @JsonIgnore
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @JsonProperty
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Long getItemId() {
        return itemId;
    }

    @JsonIgnore
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    @JsonIgnore
    public Long getUserId() {
        return userId;
    }

    @JsonIgnore
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(getMessage(), feedback.getMessage()) &&
                Objects.equals(getRating(), feedback.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMessage(), getRating());
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", rating=" + rating +
                '}';
    }

    public Feedback toSeller() {
        return new SellerFeedback(id, message, created, updated, rating, itemId, userId);
    }

    public Feedback toBuyer() {
        return new BuyerFeedback(id, message, created, updated, rating, itemId, userId);
    }
}
