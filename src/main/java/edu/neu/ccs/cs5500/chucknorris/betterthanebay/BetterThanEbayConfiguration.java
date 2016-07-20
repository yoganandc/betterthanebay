package edu.neu.ccs.cs5500.chucknorris.betterthanebay;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.*;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class BetterThanEbayConfiguration extends Configuration {
    
	@Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;
   
    @JsonProperty
    public DataSourceFactory getDataSourceFactory() {

        return database;
    }

    @JsonProperty
    public void setDataSourceFactory(DataSourceFactory database) {

        this.database = database;
    }

}
