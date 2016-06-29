package edu.neu.ccs.cs5500.chucknorris.betterthanebay.db;

import org.hibernate.tuple.AnnotationValueGeneration;
import org.hibernate.tuple.GenerationTiming;
import org.hibernate.tuple.ValueGenerator;

/**
 * Created by yoganandc on 6/29/16.
 */
public class CreatedTimestampValueGeneration implements AnnotationValueGeneration<CreatedTimestamp> {

    @Override
    public void initialize(CreatedTimestamp createdTimestamp, Class<?> aClass) {
    }

    @Override
    public GenerationTiming getGenerationTiming() {
        return GenerationTiming.INSERT;
    }

    @Override
    public ValueGenerator<?> getValueGenerator() {
        return null;
    }

    @Override
    public boolean referenceColumnInSql() {
        return true;
    }

    @Override
    public String getDatabaseGeneratedReferencedColumnValue() {
        return "localtimestamp";
    }
}
