package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    private String first;

    private String middle;

    @Column(nullable = false)
    @NotBlank
    private String last;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthday;

    public Person() {

    }

    public Person(Person obj) {
        this.id = new Long(obj.getId());
        this.first = new String(obj.getFirst());
        if(obj.getMiddle() != null) {
            this.middle = new String(obj.getMiddle());
        }
        this.last = new String(obj.getLast());
        if(obj.birthday != null) {
            this.birthday = new Date(obj.getBirthday().getTime());
        }
    }

    public Person(Long id, String first, String middle, String last, Date birthday) {
        this.id = id;
        this.first = first;
        this.middle = middle;
        this.last = last;
        this.birthday = birthday;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirst() {
        return this.first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getMiddle() {
        return this.middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getLast() {
        return this.last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getFirst(), person.getFirst()) &&
                Objects.equals(getMiddle(), person.getMiddle()) &&
                Objects.equals(getLast(), person.getLast()) &&
                Objects.equals(getBirthday(), person.getBirthday());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirst(), getMiddle(), getLast(), getBirthday());
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", first='" + first + '\'' +
                ", middle='" + middle + '\'' +
                ", last='" + last + '\'' +
                ", birthday=" + birthday +
                '}';
    }
}
