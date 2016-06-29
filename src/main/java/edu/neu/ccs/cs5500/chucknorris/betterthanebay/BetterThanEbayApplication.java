package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import de.thomaskrille.dropwizard_template_config.TemplateConfigBundle;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.api.Address;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.api.Payment;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.db.UserDAO;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.api.User;

public class BetterThanEbayApplication extends Application<BetterThanEbayConfiguration> {

    private final HibernateBundle<BetterThanEbayConfiguration> userDb = new HibernateBundle<BetterThanEbayConfiguration>(User.class) {

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
        bootstrap.addBundle(userDb);
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

        UserDAO userDAO = new UserDAO(userDb.getSessionFactory());
        UserResource userResource = new UserResource(userDAO);

        environment.jersey().register(userResource);
    }

}
