package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;

import org.hibernate.SessionFactory;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.BetterThanEbayAuthenticator;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.auth.PasswordUtil;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Address;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.BuyerFeedback;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Category;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Feedback;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Payment;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.Person;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.SellerFeedback;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.State;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.core.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.BidDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.CategoryDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.FeedbackDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.ItemDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.StateDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.BidResource;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.CategoryResource;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.FeedbackResource;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.ItemResource;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.StateResource;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class BetterThanEbayApplication extends Application<BetterThanEbayConfiguration> {

    private final HibernateBundle<BetterThanEbayConfiguration> db = new HibernateBundle<BetterThanEbayConfiguration>(Person.class, State.class,
            Address.class, Payment.class, User.class, Bid.class, Category.class, BuyerFeedback.class, SellerFeedback.class, Item.class) {

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
        bootstrap.addBundle(new SwaggerBundle<BetterThanEbayConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(BetterThanEbayConfiguration configuration) {
                return configuration.swaggerBundleConfiguration;
            }
        });
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
        PasswordUtil util = new PasswordUtil();

        CategoryDAO categoryDAO = new CategoryDAO(sf);
        CategoryResource categoryResource = new CategoryResource(categoryDAO);

        StateDAO stateDAO = new StateDAO(sf);
        StateResource stateResource = new StateResource(stateDAO);

        UserDAO userDAO = new UserDAO(sf);
        ItemDAO itemDAO = new ItemDAO(sf);
        BidDAO bidDAO = new BidDAO(sf);
        FeedbackDAO feedbackDAO = new FeedbackDAO(sf);

        Class[] bidArgTypes = {BidDAO.class, ItemDAO.class};
        Object[] bidArgs = {bidDAO, itemDAO};
        BidResource bidResource = new UnitOfWorkAwareProxyFactory(db).create(BidResource.class, bidArgTypes, bidArgs);

        Class[] feedbackArgTypes = {FeedbackDAO.class, ItemDAO.class, BidDAO.class};
        Object[] feedbackArgs = {feedbackDAO, itemDAO, bidDAO};
        FeedbackResource feedbackResource = new UnitOfWorkAwareProxyFactory(db).create(FeedbackResource.class, feedbackArgTypes, feedbackArgs);

        UserResource userResource = new UserResource(userDAO, itemDAO, bidDAO, feedbackDAO, util);
        ItemResource itemResource = new ItemResource(itemDAO, bidDAO, feedbackResource, bidResource);

        Class constructorArgTypes[] = {UserDAO.class, PasswordUtil.class};
        Object constructorArgs[] = {userDAO, util};

        BetterThanEbayAuthenticator auth = new UnitOfWorkAwareProxyFactory(db).create(BetterThanEbayAuthenticator.class,
                                                                                  constructorArgTypes, constructorArgs);
        environment.jersey().register(new AuthDynamicFeature(
                        new BasicCredentialAuthFilter.Builder<User>().setAuthenticator(auth)
                        .setRealm("AUTHENTICATED").buildAuthFilter()));
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(categoryResource);
        environment.jersey().register(stateResource);
        environment.jersey().register(userResource);
        environment.jersey().register(itemResource);
    }

}
