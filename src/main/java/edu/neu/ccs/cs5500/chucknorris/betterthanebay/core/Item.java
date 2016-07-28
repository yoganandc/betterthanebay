package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import io.dropwizard.validation.ValidationMethod;

@Entity
@Table(name = "item")
@NamedQueries(value = {
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsForUser",
                query = "SELECT i FROM Item i WHERE i.userId = :user_id"),
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getActiveItemsForUser",
                query = "SELECT i FROM Item i WHERE i.userId = :user_id AND i.endDate > CURRENT_TIMESTAMP AND i.startDate < CURRENT_TIMESTAMP"),
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsWithCategory",
                query = "SELECT i FROM Item i JOIN i.categories c WHERE c.id = :category_id " +
                        "AND i.startDate < CURRENT_TIMESTAMP AND i.endDate > CURRENT_TIMESTAMP AND i.initialPrice >= :start_price " +
                        "AND i.initialPrice <= :end_price AND i.name LIKE :query AND i.description LIKE :query " +
                        "ORDER BY i.endDate ASC"),
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item.getItemsWithoutCategory",
                query = "SELECT i FROM Item i " +
                        "WHERE i.startDate < CURRENT_TIMESTAMP AND i.endDate > CURRENT_TIMESTAMP AND i.initialPrice >= :start_price " +
                        "AND i.initialPrice <= :end_price AND i.name LIKE :query AND i.description LIKE :query " +
                        "ORDER BY i.endDate ASC")
})
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

    @Column(name = "user_id", nullable = false)
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
                BigDecimal initialPrice, Date startDate, Date endDate, String image, Long userId, Date created, Date updated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.initialPrice = initialPrice;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
        this.userId = userId;
        this.created = created;
        this.updated = updated;
    }

    public Item(Item obj) {
        if(obj.getId() != null) {
            this.id = new Long(obj.getId());
        }
        this.name = new String(obj.getName());
        if(obj.getDescription() != null) {
            this.description = new String(obj.getDescription());
        }
        for (Category category : obj.getCategories()) {
            this.categories.add(new Category(category));
        }
        this.initialPrice = new BigDecimal(obj.getInitialPrice().toString());
        this.startDate = new Date(obj.getStartDate().getTime());
        this.endDate = new Date(obj.getEndDate().getTime());
        if(obj.getImage() != null) {
            this.image = new String(obj.getImage());
        }
        if(obj.getUserId() != null) {
            this.userId = new Long(obj.getUserId());
        }
        this.created = new Date(obj.getCreated().getTime());
        this.updated = new Date(obj.getUpdated().getTime());
    }

    @JsonProperty
    public Long getId() {
        return this.id;
    }

    @JsonIgnore
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

    @JsonIgnore
    public Long getUserId() {
        return this.userId;
    }

    @JsonIgnore
    public void setUserId(Long userId) {
        this.userId = userId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(getName(), item.getName()) &&
                Objects.equals(getDescription(), item.getDescription()) &&
                Objects.equals(getCategories(), item.getCategories()) &&
                Objects.equals(getInitialPrice(), item.getInitialPrice()) &&
                Objects.equals(getStartDate(), item.getStartDate()) &&
                Objects.equals(getEndDate(), item.getEndDate()) &&
                Objects.equals(getImage(), item.getImage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getCategories(), getInitialPrice(), getStartDate(), getEndDate(), getImage());
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", categories=" + categories +
                ", initialPrice=" + initialPrice +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", image='" + image + '\'' +
                ", userId=" + userId +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }
}
