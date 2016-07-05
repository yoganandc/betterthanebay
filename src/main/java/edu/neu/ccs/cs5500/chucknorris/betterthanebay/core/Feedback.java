package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.CreatedTimestamp;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UpdatedTimestamp;

public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedTimestamp
    private Date created;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @UpdatedTimestamp
    private Date updated;

    @Column(nullable = false)
    private Integer rating;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Integer getRating() {
        return this.rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(getId(), feedback.getId()) &&
                Objects.equals(getMessage(), feedback.getMessage()) &&
                Objects.equals(getCreated(), feedback.getCreated()) &&
                Objects.equals(getUpdated(), feedback.getUpdated()) &&
                Objects.equals(getRating(), feedback.getRating());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getMessage(), getCreated(), getUpdated(), getRating());
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", rating=" + rating +
                '}';
    }
}
