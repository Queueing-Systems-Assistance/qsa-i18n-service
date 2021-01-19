package com.unideb.qsa.i18n.domain.exception;

/**
 * Exception for internal server errors.
 */
public class QSAInternalException extends RuntimeException {

    public QSAInternalException() {
    }

    public QSAInternalException(String message) {
        super(message);
    }

}
