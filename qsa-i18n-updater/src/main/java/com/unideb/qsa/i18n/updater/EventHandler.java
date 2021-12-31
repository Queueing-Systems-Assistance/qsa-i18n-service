package com.unideb.qsa.i18n.updater;

import java.util.Collection;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.unideb.qsa.i18n.domain.I18nElement;

/**
 * Handler for AWS Lambda calls.
 */
public class EventHandler implements RequestHandler<Collection<I18nElement>, Collection<String>> {

    private final I18nKeyUpdater i18nKeyUpdater;

    public EventHandler() {
        i18nKeyUpdater = new I18nKeyUpdater(dynamoDBMapper());
    }

    /**
     * Handles AWS Lambda call.
     * @param i18nKeys the keys that needs to be inserted to the database
     */
    @Override
    public Collection<String> handleRequest(Collection<I18nElement> i18nKeys, Context context) {
        return i18nKeyUpdater.update(i18nKeys);
    }

    private DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build());
    }
}
