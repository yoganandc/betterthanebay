package edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth;

import com.google.common.base.Optional;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * Created by yoganandc on 7/13/16.
 */
public class BetterThanEbayAuthenticator implements Authenticator<BasicCredentials, User> {

    private final UserDAO userDAO;
    private final PasswordUtil util;

    public BetterThanEbayAuthenticator(UserDAO userDAO, PasswordUtil util) {
        this.userDAO = userDAO;
        this.util = util;
    }

    @Override
    @UnitOfWork
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {

        User loggedInUser = userDAO.findByCredentials(basicCredentials.getUsername());

        if(loggedInUser == null || !util.authenticate(basicCredentials.getPassword().toCharArray(), loggedInUser.getPassword())) {
            return Optional.absent();
        }

        return Optional.of(loggedInUser);
    }
}
