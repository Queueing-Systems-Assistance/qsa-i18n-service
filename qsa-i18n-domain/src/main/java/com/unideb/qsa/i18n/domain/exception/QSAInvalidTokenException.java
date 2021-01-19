package com.unideb.qsa.i18n.domain.exception;

/**
 * Exception for invalid token. The token is used for validate the request, so that the i18n keys can be updated.
 */
public class QSAInvalidTokenException extends QSAInternalException {

    public QSAInvalidTokenException(String message) {
        super(message);
    }
}
