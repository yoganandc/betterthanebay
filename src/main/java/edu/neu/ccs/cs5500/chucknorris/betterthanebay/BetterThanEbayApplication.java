package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import org.hibernate.SessionFactory;
import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.BetterThanEbayAuthenticator;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.*;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.ItemResource;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;

public class BetterThanEbayApplication extends Application<BetterThanEbayConfiguration> {

    private final HibernateBundle<BetterThanEbayConfiguration> db = new HibernateBundle<BetterThanEbayConfiguration>(Person.class, State.class,
            Address.class, Payment.class, User.class, Bid.class, Category.class, Feedback.class, Item.class) {

        @Override
        public DataSourceFactory getDataSourceFactory(BetterThanEbayConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new BetterThanEbayApplication().run(args);
    }

    @Override
    public String getName() {
        return "BetterThanEbay";
    }

    @Override
    public void initialize(final Bootstrap<BetterThanEbayConfiguration> bootstrap) {
        bootstrap.addBundle(new TemplateConfigBundle());
        bootstrap.addBundle(db);
        bootstrap.addBundle(new MigrationsBundle<BetterThanEbayConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(BetterThanEbayConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final BetterThanEbayConfiguration configuration,
                    final Environment environment) {

        SessionFactory sf = db.getSessionFactory();

        UserDAO userDAO = new UserDAO(sf);
        UserResource userResource = new UserResource(userDAO);

        ItemDAO itemDAO = new ItemDAO(sf);
        BidDAO bidDAO = new BidDAO(sf);
        FeedbackDAO feedbackDAO = new FeedbackDAO(sf);
        ItemResource itemResource = new ItemResource(itemDAO, bidDAO, feedbackDAO);

        BetterThanEbayAuthenticator auth = new UnitOfWorkAwareProxyFactory(db).create(BetterThanEbayAuthenticator.class,
                                                                                  UserDAO.class, userDAO);
        environment.jersey().register(new AuthDynamicFeature(
                        new BasicCredentialAuthFilter.Builder<User>().setAuthenticator(auth)
                        .setRealm("AUTHENTICATED").buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(userResource);
        environment.jersey().register(itemResource);
    }

}
