package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date expiry;

    @OneToOne(cascade = CascadeType.ALL, optional = false, orphanRemoval = true)
    //@JoinColumn(name = "address_id", nullable = false)
    @PrimaryKeyJoinColumn
    @Valid
    private Address address;

    @Column(nullable = false)
    private Integer csv;

    @ManyToOne
    private User user;

    @JsonIgnore
    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpiry() {
        return this.expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public Address getAddress() {
        return this.address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getCsv() {
        return this.csv;
    }

    public void setCsv(Integer csv) {
        this.csv = csv;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(getId(), payment.getId()) &&
                Objects.equals(getFirstName(), payment.getFirstName()) &&
                Objects.equals(getLastName(), payment.getLastName()) &&
                Objects.equals(getNumber(), payment.getNumber()) &&
                Objects.equals(getExpiry(), payment.getExpiry()) &&
                Objects.equals(getAddress(), payment.getAddress()) &&
                Objects.equals(getCsv(), payment.getCsv());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName(), getNumber(), getExpiry(), getAddress(), getCsv());
    }

    @Override
    public String toString() {
        return "Payment{" +
                "csv=" + csv +
                ", address=" + address +
                ", expiry=" + expiry +
                ", number='" + number + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", id=" + id +
                '}';
    }
}
