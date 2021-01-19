package com.unideb.qsa.i18n.domain.exception;

import java.util.List;

import com.unideb.qsa.i18n.domain.I18nElement;

/**
 * Exception for batch update.
 */
public class QSABatchUpdateException extends QSAInternalException {

    private final List<I18nElement> i18nElements;
    private final List<Exception> failedBatchExceptions;

    public QSABatchUpdateException(List<I18nElement> i18nElements, List<Exception> failedBatchExceptions) {
        this.i18nElements = i18nElements;
        this.failedBatchExceptions = failedBatchExceptions;
    }

    public List<I18nElement> getI18nElements() {
        return i18nElements;
    }

    public List<Exception> getFailedBatchExceptions() {
        return failedBatchExceptions;
    }
}
