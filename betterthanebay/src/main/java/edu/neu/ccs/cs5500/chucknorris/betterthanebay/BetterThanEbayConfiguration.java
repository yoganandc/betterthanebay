package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;

import edu.neu.ccs.cs5500.chucknorris.betterthanebay.BetterThanEbayDatabaseConfiguration;

public class BetterThanEbayConfiguration extends Configuration {
    
	@Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();
   
    @JsonProperty
    public DataSourceFactory getDataSourceFactory() {
        
        // DatabaseConfiguration databaseConfiguration = BetterThanEbayDatabaseConfiguration.create(System.getenv("DATABASE_URL"));
        // database = databaseConfiguration.getDataSourceFactory(null);

        return database;
    }

    @JsonProperty
    public void setDataSourceFactory() {
        this.database = database;
    }

}
