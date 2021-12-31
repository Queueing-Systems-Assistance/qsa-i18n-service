package com.unideb.qsa.i18n.retriever;

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
public class EventHandler implements RequestHandler<Collection<String>, Collection<I18nElement>> {

    private final I18nKeyRetriever i18nKeyRetriever;

    public EventHandler() {
        i18nKeyRetriever = new I18nKeyRetriever(dynamoDBMapper());
    }

    @Override
    public Collection<I18nElement> handleRequest(Collection<String> i18nKeys, Context context) {
        return i18nKeyRetriever.resolveI18nKeys(i18nKeys);
    }

    private DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .build());
    }
}
