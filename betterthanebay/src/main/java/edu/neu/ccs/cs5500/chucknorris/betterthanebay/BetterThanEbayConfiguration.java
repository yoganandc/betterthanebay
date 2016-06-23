package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import io.dropwizard.db.DataSourceFactory;

public class BetterThanEbayConfiguration extends Configuration {
    
	@Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();
   
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    public void setDataSourceFactory() {
        this.database = database;
    }

}
