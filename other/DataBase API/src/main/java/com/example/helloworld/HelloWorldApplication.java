package com.example.helloworld;

import org.skife.jdbi.v2.DBI;

import com.example.helloworld.health.TemplateHealthCheck;
import com.example.helloworld.resources.HelloWorldResource;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Environment;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {
  public static void main(String[] args) throws Exception {
    new HelloWorldApplication().run(args);
  }

  @Override
  public String getName() {
    return "hello-world";
  }

  // private final HibernateBundle<HelloWorldConfiguration> hibernateBundle =
  // new HibernateBundle<HelloWorldConfiguration>(User.class) {
  //
  // public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
  // return configuration.getDataSourceFactory();
  // }
  // };
  //
  // @Override
  // public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
  // bootstrap.addBundle(this.hibernateBundle);
  // }

  @Override
  public void run(HelloWorldConfiguration configuration, Environment environment)
      throws ClassNotFoundException {
    final HelloWorldResource resource =
        new HelloWorldResource(configuration.getTemplate(), configuration.getDefaultName());

    final TemplateHealthCheck healthCheck = new TemplateHealthCheck(configuration.getTemplate());
    environment.healthChecks().register("template", healthCheck);
    environment.jersey().register(resource);

    // final HibernateBundle<HelloWorldConfiguration> hibernateBundle = new
    // HibernateBundle<HelloWorldConfiguration>(User.class) {
    // public DataSourceFactory getDataSourceFactory(HelloWorldConfiguration configuration) {
    // return configuration.getDataSourceFactory();
    // }
    //
    //
    // @Override
    // public void initialize(
    // final Bootstrap<HelloWorldConfiguration> bootstrap) {
    // bootstrap.addBundle(hibernateBundle);
    // }



    // DATABASE
    final DBIFactory factory = new DBIFactory();
    final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
    // final UserDAO dao = jdbi.onDemand(UserDAO.class);
    // environment.jersey().register(new UserResource(dao));
  }

}
