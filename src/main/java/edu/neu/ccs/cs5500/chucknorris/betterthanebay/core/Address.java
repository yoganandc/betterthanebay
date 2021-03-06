package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name = "address")
public class Address implements Comparable<Address> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @ApiModelProperty(required = true)
    private String line1;

    private String line2;

    @Column(nullable = false)
    @NotBlank
    @ApiModelProperty(required = true)
    private String city;

    @OneToOne(optional = false)
    @JoinColumn(name = "state_id", nullable = false)
    @Valid
    @NotNull
    private State state;

    @Column(nullable = false)
    @NotBlank
    @ApiModelProperty(required = true)
    private String zip;

    public Address() {

    }

    public Address(Address obj) {
        if(obj.getId() != null) {
            this.id = new Long(obj.getId());
        }
        this.line1 = new String(obj.getLine1());
        if(obj.getLine2() != null) {
            this.line2 = new String(obj.getLine2());
        }
        this.city = new String(obj.getCity());
        this.state = new State(obj.getState());
        this.zip = new String(obj.getZip());
    }

    public Address(Long id, String line1, String line2, String city, State state, String zip) {
        this.id = id;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    @JsonProperty
    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    public String getLine1() {
        return this.line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return this.line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getZip() {
        return this.zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getLine1(), address.getLine1()) &&
                Objects.equals(getLine2(), address.getLine2()) &&
                Objects.equals(getCity(), address.getCity()) &&
                Objects.equals(getState(), address.getState()) &&
                Objects.equals(getZip(), address.getZip());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLine1(), getLine2(), getCity(), getState(), getZip());
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", city='" + city + '\'' +
                ", state=" + state +
                ", zip='" + zip + '\'' +
                '}';
    }

    @Override
    public int compareTo(Address o) {
        if(this == o) {
            return 0;
        }

        int comparison = line1.compareTo(o.line1);
        if(comparison != 0) {
            return comparison;
        }

        if(line2 == null && o.line2 != null) {
            return -1;
        }
        if(line2 != null && o.line2 == null) {
            return 1;
        }
        if(line2 != null && o.line2 != null) {
            comparison = line2.compareTo(o.line2);
            if(comparison != 0) {
                return comparison;
            }
        }

        comparison = city.compareTo(o.city);
        if(comparison != 0) {
            return comparison;
        }

        comparison = state.compareTo(o.state);
        if(comparison != 0) {
            return comparison;
        }

        comparison = zip.compareTo(o.zip);
        if(comparison != 0) {
            return comparison;
        }

        return 0;
    }
}
