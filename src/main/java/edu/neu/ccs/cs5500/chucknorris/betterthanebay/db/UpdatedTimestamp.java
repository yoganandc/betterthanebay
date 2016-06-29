package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yoganandc on 6/29/16.
 */

@ValueGenerationType(generatedBy = UpdatedTimestampValueGeneration.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdatedTimestamp {
}
