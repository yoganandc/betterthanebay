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
 * Created by yoganandc on 6/29/16.
 */

@Entity
@Table(name = "`state`")
@NamedQueries(value = {
        @NamedQuery(name = "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.State.findAll",
                query = "SELECT s FROM State s")
})
public class State implements Comparable<State> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @ApiModelProperty(required = true)
    private String name;

    @Column(nullable = false, unique = true)
    @NotBlank
    @ApiModelProperty(required = true)
    private String code;

    public State() {

    }

    public State(State obj) {
        this.id = new Long(obj.getId());
        this.name = new String(obj.getName());
        this.code = new String(obj.getCode());
    }

    public State(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(getId(), state.getId()) &&
                Objects.equals(getName(), state.getName()) &&
                Objects.equals(getCode(), state.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCode());
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    @Override
    public int compareTo(State o) {
        return this.id.compareTo(o.id);
    }
}
