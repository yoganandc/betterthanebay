package com.example.helloworld;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

@RegisterMapper(UserMapper.class)
public interface UserDAO {

  @SqlQuery("select * from public.user")
  List<User> getAll();

  @SqlQuery("select * from User where ID = :id")
  User findById(@Bind("id") Long id);

  @SqlUpdate("delete from User where ID = :id")
  long deleteById(@Bind("id") Long id);

  @SqlUpdate("update into User set username = :username where ID = :id")
  long update(@BindBean User user);

  @SqlUpdate("insert into User (ID, username) values (:id, :username)")
  long insert(@BindBean User user);
}


