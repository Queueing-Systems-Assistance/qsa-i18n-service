package com.unideb.qsa.i18n.updater;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.domain.exception.QSABatchUpdateException;

/**
 * Update i18n keys in a batch.
 */
public class I18nKeyUpdater {

    private final DynamoDBMapper dynamoDBMapper;

    public I18nKeyUpdater(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Update the given i18n elements. Each element should container the default locale (en_US).
     * @param i18nElements i18n elements
     */
    public Collection<String> update(Collection<I18nElement> i18nElements) {
        List<DynamoDBMapper.FailedBatch> failedBatches = dynamoDBMapper.batchSave(i18nElements);
        if (!failedBatches.isEmpty()) {
            throw new QSABatchUpdateException(i18nElements, getExceptions(failedBatches));
        }
        return i18nElements.stream().map(I18nElement::getKey).collect(Collectors.toList());
    }

    private List<Exception> getExceptions(List<DynamoDBMapper.FailedBatch> failedBatches) {
        return failedBatches.stream()
                            .map(DynamoDBMapper.FailedBatch::getException)
                            .collect(Collectors.toList());
    }
}
