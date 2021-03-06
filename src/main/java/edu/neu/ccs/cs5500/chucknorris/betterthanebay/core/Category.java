package edu.neu.ccs.cs5500.chucknorris.betterthanebay.core;

import org.hibernate.validator.constraints.NotBlank;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by yoganandc on 7/1/16.
 */

@Entity
@Table(name = "category")
@NamedQueries(value = {
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Category.findAll",
        query = "SELECT c FROM Category c")
})
public class Category implements Comparable<Category> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @ApiModelProperty(required = true)
    private String name;

    public Category() {

    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Category obj) {
        this(new Long(obj.getId()), new String(obj.getName()));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId()) &&
                Objects.equals(getName(), category.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int compareTo(Category o) {
        return this.id.compareTo(o.getId());
    }
}
