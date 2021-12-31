package com.unideb.qsa.i18n.domain.exception;

import java.util.Collection;

import com.unideb.qsa.i18n.domain.I18nElement;

/**
 * Exception for batch update.
 */
public class QSABatchUpdateException extends QSAInternalException {

    private final Collection<I18nElement> i18nElements;
    private final Collection<Exception> failedBatchExceptions;

    public QSABatchUpdateException(Collection<I18nElement> i18nElements, Collection<Exception> failedBatchExceptions) {
        this.i18nElements = i18nElements;
        this.failedBatchExceptions = failedBatchExceptions;
    }

    public Collection<I18nElement> getI18nElements() {
        return i18nElements;
    }

    public Collection<Exception> getFailedBatchExceptions() {
        return failedBatchExceptions;
    }
}
