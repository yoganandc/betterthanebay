package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.Query;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.PasswordUtil;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Payment;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import io.dropwizard.hibernate.AbstractDAO;

/**
 * Created by yoganandc on 6/28/16.
 */
public class UserDAO extends AbstractDAO<User> {

    PasswordUtil util = new PasswordUtil();

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    // Find User by ID
    public User findById(Long id) {
        return get(id);
    }

    // Create new User
    public User create(User user) {
        user.setPassword(util.hash(user.getPassword().toCharArray()));
        for(Payment payment : user.getPayments()) {
            payment.setUser(user);
        }
        user.setCreated(new Date());
        user.setUpdated(new Date());

        return persist(user);
    }

    // Update User with given information (have to check how it works)
    public User update(User user) {
        currentSession().clear();
        for(Payment payment : user.getPayments()) {
            payment.setUser(user);
        }
        user.setUpdated(new Date());
        return persist(user);
    }

    public boolean deleteUser(Long id) {
        User user = get(id);
        if(user == null) {
            return false;
        }
        else {
            super.currentSession().delete(user);
            return true;
        }
    }

    public User findByCredentials(String username, String password) {
        Query userQuery =
                super.namedQuery("edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User.getByUsername")
                        .setParameter("username", username);
        User user = super.uniqueResult(userQuery);

        if ((user != null) && this.util.authenticate(password.toCharArray(), user.getPassword())) {
            return user;
        }

        return null;
    }

    public List<User> searchByUsername(String optional, int start, int size) {
        Query query = super.namedQuery(
                "edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User.searchByUsername");
        query.setParameter("username", "%" + optional + "%");
        return list(query);
    }
}
