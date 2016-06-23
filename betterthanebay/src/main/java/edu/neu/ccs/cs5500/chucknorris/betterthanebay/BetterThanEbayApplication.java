package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.migrations.MigrationsBundle;

public class BetterThanEbayApplication extends Application<BetterThanEbayConfiguration> {

    private final HibernateBundle<TutorialConfiguration> hibernate = new HibernateBundle<TutorialConfiguration>(Saying.class) {

        @Override
        public DataSourceFactory getDataSourceFactory(TutorialConfiguration configuration) {
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
        bootstrap.addBundle(hibernate);

        bootstrap.addBundle(new MigrationsBundle<ExampleConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ExampleConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final BetterThanEbayConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }

}
