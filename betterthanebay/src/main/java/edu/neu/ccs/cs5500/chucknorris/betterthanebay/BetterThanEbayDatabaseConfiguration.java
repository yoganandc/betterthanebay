package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;

import java.net.URI;
import java.net.URISyntaxException;

public class BetterThanEbayDatabaseConfiguration { //implements DatabaseConfiguration {

	// private static DatabaseConfiguration databaseConfiguration;

	// public static DatabaseConfiguration create(String databaseUrl) {

	// 	if(databaseUrl == null) {
	// 		throw new IllegalArgumentException("DATABASE_URL must be set up first.");
	// 	}

	// 	DatabaseConfiguration databaseConfiguration = null;

	// 	try {
	// 		URI dbUri = new URI(databaseUrl);
	// 		final String user = dbUri.getUserInfo().split(":")[0];
	// 		final String password = dbUri.getUserInfo().split(":")[1];
	// 		final String url = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

	// 		databaseConfiguration = new DatabaseConfiguration() {
 //                DataSourceFactory dataSourceFactory;

 //                @Override
 //                public DataSourceFactory getDataSourceFactory(Configuration configuration) {
 //                    if (dataSourceFactory!= null) {
 //                        return dataSourceFactory;
 //                    }
 //                    DataSourceFactory dsf = new DataSourceFactory();
 //                    dsf.setUser(user);
 //                    dsf.setPassword(password);
 //                    dsf.setUrl(url);
 //                    dsf.setDriverClass("org.postgresql.Driver");
 //                    dataSourceFactory = dsf;
 //                    return dsf;
 //                }
 //            };
	// 	}
	// 	catch(URISyntaxException e) {

	// 	}

	// 	return databaseConfiguration;
	// }

	// @Override
 //    public DataSourceFactory getDataSourceFactory(Configuration configuration) {
        
 //        if (databaseConfiguration == null) {
 //            throw new IllegalStateException("You must first call DatabaseConfiguration.create(dbUrl)");
 //        }
 //        return databaseConfiguration.getDataSourceFactory(null);
 //    }
}
