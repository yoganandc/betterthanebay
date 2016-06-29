package com.example.helloworld;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {
  /**
   * Entity's unique identifier.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty
  private Long id;

  /**
   * user person_id
   */
  @JsonProperty
  @Column(name = "person_id")
  private String person_id;

  /**
   * user username.
   */
  @JsonProperty
  @Column(name = "username")
  private String username;
  /**
   * user password
   */
  @JsonProperty
  @Column(name = "password")
  private String password;

  /**
   * user rating.
   */
  @JsonProperty
  @Column(name = "rating")
  private String rating;

  /**
   * user created time stamp.
   */
  @JsonProperty
  @Column(name = "created")
  private String created;

  /**
   * user updated time stamp.
   */
  @JsonProperty
  @Column(name = "updated")
  private String updated;

  /**
   * A no-argument constructor.
   */

  public User() {}

  /**
   * @param id
   * @param person_id
   * @param username
   * @param password
   * @param rating
   * @param created
   * @param updated
   */
  public User(Long id, String person_id, String username, String password, String rating,
      String created, String updated) {
    super();
    this.id = id;
    this.person_id = person_id;
    this.username = username;
    this.password = password;
    this.rating = rating;
    this.created = created;
    this.updated = updated;
  }

  /**
   * @return the id
   */
  public Long getId() {
    return this.id;
  }

  /**
   * @param id the id to set
   */
  public User setId(Long id) {
    this.id = id;
    return this;
  }

  /**
   * @return the person_id
   */
  public String getPerson_id() {
    return this.person_id;
  }

  /**
   * @param person_id the person_id to set
   */
  public User setPerson_id(String person_id) {
    this.person_id = person_id;
    return this;
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
  public User setUsername(String username) {
    this.username = username;
    return this;
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
  public User setPassword(String password) {
    this.password = password;
    return this;
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
  public User setRating(String rating) {
    this.rating = rating;
    return this;
  }

  /**
   * @return the created
   */
  public String getCreated() {
    return this.created;
  }

  /**
   * @param created the created to set
   */
  public User setCreated(String created) {
    this.created = created;
    return this;
  }

  /**
   * @return the updated
   */
  public String getUpdated() {
    return this.updated;
  }

  /**
   * @param updated the updated to set
   */
  public User setUpdated(String updated) {
    this.updated = updated;
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((this.created == null) ? 0 : this.created.hashCode());
    result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
    result = (prime * result) + ((this.password == null) ? 0 : this.password.hashCode());
    result = (prime * result) + ((this.person_id == null) ? 0 : this.person_id.hashCode());
    result = (prime * result) + ((this.rating == null) ? 0 : this.rating.hashCode());
    result = (prime * result) + ((this.updated == null) ? 0 : this.updated.hashCode());
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
    if (this.created == null) {
      if (other.created != null) {
        return false;
      }
    } else if (!this.created.equals(other.created)) {
      return false;
    }
    if (this.id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!this.id.equals(other.id)) {
      return false;
    }
    if (this.password == null) {
      if (other.password != null) {
        return false;
      }
    } else if (!this.password.equals(other.password)) {
      return false;
    }
    if (this.person_id == null) {
      if (other.person_id != null) {
        return false;
      }
    } else if (!this.person_id.equals(other.person_id)) {
      return false;
    }
    if (this.rating == null) {
      if (other.rating != null) {
        return false;
      }
    } else if (!this.rating.equals(other.rating)) {
      return false;
    }
    if (this.updated == null) {
      if (other.updated != null) {
        return false;
      }
    } else if (!this.updated.equals(other.updated)) {
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
