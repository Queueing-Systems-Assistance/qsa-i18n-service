package com.unideb.qsa.i18n.implementation.updater;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.domain.exception.QSABatchUpdateException;

/**
 * Update i18n keys in a batch.
 */
@Component
public class I18nKeyUpdater {

    private final DynamoDBMapper dynamoDBMapper;

    public I18nKeyUpdater(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Update the given i18n elements. Each element should container the default locale (en_US).
     * @param i18nElements i18n elements
     */
    public void update(List<I18nElement> i18nElements) {
        List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBMapper.batchSave(i18nElements);
        if (!failedBatches.isEmpty()) {
            throw new QSABatchUpdateException(i18nElements, getExceptions(failedBatches));
        }
    }

    private List<Exception> getExceptions(List<DynamoDBMapper.FailedBatch> failedBatches) {
        return failedBatches.stream().map(DynamoDBMapper.FailedBatch::getException).collect(Collectors.toList());
    }
}
