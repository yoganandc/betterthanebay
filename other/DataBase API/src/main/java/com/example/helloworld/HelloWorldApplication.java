package com.example.helloworld;



import org.skife.jdbi.v2.DBI;

import com.example.helloworld.resources.UserResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;



public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
  public static void main(String[] args) throws Exception {
    new HelloWorldApplication().run(args);
  }


  // @Override
  // public String getName() {
  // return "hello-world";
  // }

  @Override
  public String getName() {
    return "dropwizard-jdbi";
  }

  @Override
  public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
    bootstrap.addBundle(new MigrationsBundle<HelloWorldConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
        return configuration.getDataSourceFactory();
      }
    });
  }


  @Override
  public void run(HelloWorldConfiguration configuration, Environment environment)
      throws ClassNotFoundException {

    // final HelloWorldResource resource =
    // new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName());
    //
    // final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
    // environment.healthChecks().register("template", healthCheck);
    // environment.jersey().register(resource);



    // DATABASE
    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");

    final UserDAO dao = jdbi.onDemand(UserDAO.class);
    final UserResource personResource = new UserResource(dao);
    environment.jersey().register(personResource);
  }

}
