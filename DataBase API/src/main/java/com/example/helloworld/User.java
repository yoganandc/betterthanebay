package com.example.helloworld;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@NamedQueries({@NamedQuery(name = "findAll", query = "select e from User e"),
    @NamedQuery(name = "findByName", query = "select e from User e "
        + "where e.firstName like :name " + "or e.lastName like :name")})
public class User {
  /**
   * Entity's unique identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  /**
   * user first name.
   */
  @Column(name = "first_name")
  private String firstName;
  /**
   * user last name.
   */
  @Column(name = "last_name")
  private String lastName;
  /**
   * user username.
   */
  private String username;
  /**
   * user password
   */
  private String password;
  /**
   * user addressId.
   */
  private String addressId;
  /**
   * user rating.
   */
  private String rating;

  /**
   * A no-argument constructor.
   */
  public User() {}

  /**
   * User Constructor
   * 
   * @param id
   * @param firstName
   * @param lastName
   * @param username
   * @param password
   * @param addressId
   * @param rating
   */
  public User(long id, String firstName, String lastName, String username, String password,
      String addressId, String rating) {
    super();
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.password = password;
    this.addressId = addressId;
    this.rating = rating;
  }

  /**
   * @return the id
   */
  public long getId() {
    return this.id;
  }

  /**
   * @param id the id to set
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * @return the firstName
   */
  public String getFirstName() {
    return this.firstName;
  }

  /**
   * @param firstName the firstName to set
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the lastName
   */
  public String getLastName() {
    return this.lastName;
  }

  /**
   * @param lastName the lastName to set
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the username
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * @return the addressId
   */
  public String getAddressId() {
    return this.addressId;
  }

  /**
   * @param addressId the addressId to set
   */
  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }

  /**
   * @return the rating
   */
  public String getRating() {
    return this.rating;
  }

  /**
   * @param rating the rating to set
   */
  public void setRating(String rating) {
    this.rating = rating;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.addressId == null) ? 0 : this.addressId.hashCode());
    result = (prime * result) + ((this.firstName == null) ? 0 : this.firstName.hashCode());
    result = (prime * result) + (int) (this.id ^ (this.id >>> 32));
    result = (prime * result) + ((this.lastName == null) ? 0 : this.lastName.hashCode());
    result = (prime * result) + ((this.password == null) ? 0 : this.password.hashCode());
    result = (prime * result) + ((this.rating == null) ? 0 : this.rating.hashCode());
    result = (prime * result) + ((this.username == null) ? 0 : this.username.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    User other = (User) obj;
    if (this.addressId == null) {
      if (other.addressId != null) {
        return false;
      }
    } else if (!this.addressId.equals(other.addressId)) {
      return false;
    }
    if (this.firstName == null) {
      if (other.firstName != null) {
        return false;
      }
    } else if (!this.firstName.equals(other.firstName)) {
      return false;
    }
    if (this.id != other.id) {
      return false;
    }
    if (this.lastName == null) {
      if (other.lastName != null) {
        return false;
      }
    } else if (!this.lastName.equals(other.lastName)) {
      return false;
    }
    if (this.password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!this.password.equals(other.password)) {
      return false;
    }
    if (this.rating == null) {
      if (other.rating != null) {
        return false;
      }
    } else if (!this.rating.equals(other.rating)) {
      return false;
    }
    if (this.username == null) {
      if (other.username != null) {
        return false;
      }
    } else if (!this.username.equals(other.username)) {
      return false;
    }
    return true;
  }



}
