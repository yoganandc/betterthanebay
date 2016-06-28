package edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;

/**
 * Created by yoganandc on 6/28/16.
 */
public class UserResource {

    private final UserDAO dao;

    public UserResource(UserDAO dao) {
        this.dao = dao;
    }
}
