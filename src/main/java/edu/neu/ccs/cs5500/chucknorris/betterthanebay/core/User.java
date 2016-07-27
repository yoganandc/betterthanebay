package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.OrderBy;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "`user`")
@NamedQueries(value = {
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User.searchByUsername",
                query = "SELECT u FROM User u WHERE u.username LIKE :username ORDER BY u.id ASC"),
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User.getByUsername",
                query = "SELECT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User.deleteUser",
                query = "DELETE FROM User u WHERE u.id = :id")})


public class User implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    @NotBlank
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    @JoinColumn(name = "person_id", nullable = false)
    @Valid
    @NotNull
    private Person details;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinTable(name = "user_address", joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "address_id", nullable = false, unique = true))
    @Valid
    @NotEmpty
    @OrderBy(clause = "id ASC")
    private SortedSet<Address> addresses = new TreeSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true,
            fetch = FetchType.EAGER)
    @Valid
    @NotEmpty
    @OrderBy(clause = "id ASC")
    private SortedSet<Payment> payments = new TreeSet<>();

    @DecimalMax(value = "5.0")
    @DecimalMin(value = "0.0")
    private BigDecimal rating;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public User() {

    }

    public User(User obj) {
        if(obj.getId() != null) {
            this.id = new Long(obj.getId());
        }
        this.username = new String(obj.getUsername());
        this.password = new String(obj.getPassword());
        this.details = new Person(obj.getDetails());
        for(Address address : obj.getAddresses()) {
            this.getAddresses().add(new Address(address));
        }
        for(Payment payment : obj.getPayments()) {
            this.getPayments().add(new Payment(payment));
        }
        if(obj.getRating() != null) {
            this.rating = new BigDecimal(obj.getRating().toString());
        }
        this.created = new Date(obj.getCreated().getTime());
        this.updated = new Date(obj.getUpdated().getTime());
    }

    public User(Long id, String username, String password, Person details, SortedSet<Address> addresses, SortedSet<Payment> payments, BigDecimal rating, Date created, Date updated) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.details = details;
        this.addresses = addresses;
        this.payments = payments;
        this.rating = rating;
        this.created = created;
        this.updated = updated;
    }

    @JsonProperty
    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Person getDetails() {
        return this.details;
    }

    public void setDetails(Person details) {
        this.details = details;
    }

    public SortedSet<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(SortedSet<Address> addresses) {
        this.addresses = addresses;
    }

    public SortedSet<Payment> getPayments() {
        return this.payments;
    }

    public void setPayments(SortedSet<Payment> payments) {
        this.payments = payments;
    }

    @JsonProperty
    public BigDecimal getRating() {
        return this.rating;
    }

    @JsonIgnore
    public void setRating(BigDecimal rating) {
        this.rating = rating;
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
        if (this == o) {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(getId(), user.getId())
                && Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getPassword(), user.getPassword())
                && Objects.equals(getDetails(), user.getDetails())
                && Objects.equals(getAddresses(), user.getAddresses())
                && Objects.equals(getPayments(), user.getPayments())
                && Objects.equals(getRating(), user.getRating())
                && Objects.equals(getCreated(), user.getCreated())
                && Objects.equals(getUpdated(), user.getUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getPassword(), getDetails(), getAddresses(),
                getPayments(), getRating(), getCreated(), getUpdated());
    }

    @Override
    public String toString() {
        return "User{" + "id=" + this.id + ", username='" + this.username + '\'' + ", password='"
                + this.password + '\'' + ", details=" + this.details + ", addresses=" + this.addresses
                + ", payments=" + this.payments + ", rating=" + this.rating + ", created=" + this.created
                + ", updated=" + this.updated + '}';
    }

    @Override
    @JsonIgnore
    public String getName() {
        return this.getUsername();
    }

//    public void addPayment(Payment payment) {
//        payment.setUser(this);
//        this.getPayments().add(payment);
//    }
//
//    public void removePayment(Payment payment) {
//        this.getPayments().remove(payment);
//        payment.setUser(null);
//    }
//
//    public void addAddress(Address address) {
//        this.getAddresses().add(address);
//    }
//
//    public void removeAddress(Address address) {
//        this.getAddresses().remove(address);
//    }
}
