package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.BetterThanEbayConfiguration;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.resources.DemoResource;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.api.User;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.api.Bid;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.api.Item;
import edu.neu.ccs.cs5500.chucknorris.betterthanebay.api.Feedback;

public class BetterThanEbayApplication extends Application<BetterThanEbayConfiguration> {

    // private final HibernateBundle<BetterThanEbayConfiguration> userDb = new HibernateBundle<BetterThanEbayConfiguration>(User.class) {

    //     @Override
    //     public DataSourceFactory getDataSourceFactory(BetterThanEbayConfiguration configuration) {
    //         return configuration.getDataSourceFactory();
    //     }
    // };
    // private final HibernateBundle<BetterThanEbayConfiguration> itemDb = new HibernateBundle<BetterThanEbayConfiguration>(Item.class) {

    //     @Override
    //     public DataSourceFactory getDataSourceFactory(BetterThanEbayConfiguration configuration) {
    //         return configuration.getDataSourceFactory();
    //     }
    // };
    // private final HibernateBundle<BetterThanEbayConfiguration> bidDb = new HibernateBundle<BetterThanEbayConfiguration>(Bid.class) {

    //     @Override
    //     public DataSourceFactory getDataSourceFactory(BetterThanEbayConfiguration configuration) {
    //         return configuration.getDataSourceFactory();
    //     }
    // };
    // private final HibernateBundle<BetterThanEbayConfiguration> feedbackDb = new HibernateBundle<BetterThanEbayConfiguration>(Feedback.class) {

    //     @Override
    //     public DataSourceFactory getDataSourceFactory(BetterThanEbayConfiguration configuration) {
    //         return configuration.getDataSourceFactory();
    //     }
    // };

    public static void main(final String[] args) throws Exception {
        new BetterThanEbayApplication().run(args);
    }

    @Override
    public String getName() {
        return "BetterThanEbay";
    }

    @Override
    public void initialize(final Bootstrap<BetterThanEbayConfiguration> bootstrap) {
        // bootstrap.addBundle(userDb);
        // bootstrap.addBundle(itemDb);
        // bootstrap.addBundle(feedbackDb);
        // bootstrap.addBundle(bidDb);

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
        DemoResource resource = new DemoResource();
        environment.jersey().register(resource);
    }

}
