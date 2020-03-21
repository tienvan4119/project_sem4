package com.aptech.project.project_sem4.model;

import com.mongodb.DBObject;
import org.bson.Document;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;

public class CustomAggregationOperation implements AggregationOperation {
    private DBObject operation;

    public CustomAggregationOperation (DBObject operation) {
        this.operation = operation;
    }

    public Document toDBObject(AggregationOperationContext context) {
        return context.getMappedObject((Document) operation);
    }

    @Override
    public Document toDocument(AggregationOperationContext context) {
        return null;
    }
}
