package com.unideb.qsa.i18n.domain.exception;

/**
 * Exception for client errors (eg: missing i18n key).
 */
public class QSAClientException extends RuntimeException {

    public QSAClientException(String message) {
        super(message);
    }
}
