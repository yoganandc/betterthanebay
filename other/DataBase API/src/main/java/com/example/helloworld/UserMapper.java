package com.example.helloworld;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class UserMapper implements ResultSetMapper<User> {

  public User map(int index, ResultSet resultSet, StatementContext statementContext)
      throws SQLException {
    return new User().setId(resultSet.getLong("id")).setPerson_id(resultSet.getString("person_id"))
        .setUsername(resultSet.getString("username")).setPassword(resultSet.getString("password"))
        .setRating(resultSet.getString("rating")).setCreated(resultSet.getString("created"))
        .setUpdated(resultSet.getString("updated"));

  }

}
