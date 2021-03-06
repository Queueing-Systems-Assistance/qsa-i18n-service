package com.unideb.qsa.i18n.server.advice;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.List;

import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.domain.exception.QSABatchUpdateException;
import com.unideb.qsa.i18n.domain.exception.QSAClientException;
import com.unideb.qsa.i18n.domain.exception.QSAInvalidTokenException;

/**
 * Unit tests for {@link ExceptionHandlingAdvice}.
 */
public class ExceptionHandlingAdviceTest {

    private static final int EXPECTED_LOG_LIST_SIZE = 1;
    private static final String ERROR_MESSAGE = "Internal Exception occurred";
    private static final String ERROR_MESSAGE_BATCH_UPDATE = "Failed to batch update, keys";
    private static final String ERROR_MESSAGE_INVALID_TOKEN = "Given [X-QSA-Token] token is not valid!";
    private static final List<I18nElement> I18N_ELEMENTS = List.of(new I18nElement("simple.key"));
    private static final Void EMPTY_BODY = null;
    private static final RuntimeException BATCH_EXCEPTION = new RuntimeException("Test Batch Exception");

    private final ExceptionHandlingAdvice exceptionHandlingAdvice = new ExceptionHandlingAdvice();
    private ListAppender<ILoggingEvent> listAppender;

    @Mock
    private WebRequest webRequest;
    @Mock
    private HttpInputMessage httpInputMessage;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        Logger logger = (Logger) LoggerFactory.getLogger(ExceptionHandlingAdvice.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }

    @Test
    public void handleInvalidTokenException() {
        // GIVEN
        var expected = ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(EMPTY_BODY);
        // WHEN
        var actual = exceptionHandlingAdvice.handleInvalidTokenException(new QSAInvalidTokenException(ERROR_MESSAGE_INVALID_TOKEN));
        // THEN
        assertEquals(actual, expected);
        assertEquals(listAppender.list.size(), EXPECTED_LOG_LIST_SIZE);
        assertEquals(listAppender.list.get(0).getLevel(), Level.ERROR);
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains(ERROR_MESSAGE_INVALID_TOKEN));
    }

    @Test
    public void handleInternalException() {
        // GIVEN
        var expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EMPTY_BODY);
        // WHEN
        var actual = exceptionHandlingAdvice.handleInternalException(new RuntimeException());
        // THEN
        assertEquals(actual, expected);
        assertEquals(listAppender.list.size(), EXPECTED_LOG_LIST_SIZE);
        assertEquals(listAppender.list.get(0).getLevel(), Level.ERROR);
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains(ERROR_MESSAGE));
    }

    @Test
    public void handleBatchUpdateException() {
        // GIVEN
        var expected = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EMPTY_BODY);
        var exception = new QSABatchUpdateException(I18N_ELEMENTS, List.of(BATCH_EXCEPTION));
        // WHEN
        var actual = exceptionHandlingAdvice.handleBatchUpdateException(exception);
        // THEN
        assertEquals(actual, expected);
        assertEquals(listAppender.list.size(), EXPECTED_LOG_LIST_SIZE);
        assertEquals(listAppender.list.get(0).getLevel(), Level.ERROR);
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains(ERROR_MESSAGE_BATCH_UPDATE));
    }

    @Test
    public void handleClientException() {
        // GIVEN
        var expected = ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(EMPTY_BODY);
        // WHEN
        var actual = exceptionHandlingAdvice.handleClientException(new QSAClientException(ERROR_MESSAGE));
        // THEN
        assertEquals(actual, expected);
        assertEquals(listAppender.list.size(), EXPECTED_LOG_LIST_SIZE);
        assertEquals(listAppender.list.get(0).getLevel(), Level.WARN);
        assertTrue(listAppender.list.get(0).getFormattedMessage().contains(ERROR_MESSAGE));
    }


    @Test
    public void handleHttpMessageNotReadable() {
        // GIVEN
        var noHandlerFoundException = new HttpMessageNotReadableException(ERROR_MESSAGE, null, httpInputMessage);
        var expected = ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(EMPTY_BODY);
        // WHEN
        var actual = exceptionHandlingAdvice.handleHttpMessageNotReadable(noHandlerFoundException, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, webRequest);
        // THEN
        verifyNoInteractions(webRequest);
        verifyNoInteractions(httpInputMessage);
        assertEquals(actual, expected);
    }

    @Test
    public void handleNoHandlerFoundException() {
        // GIVEN
        var noHandlerFoundException = new NoHandlerFoundException("POST", "http://example.url.com", HttpHeaders.EMPTY);
        var expected = ResponseEntity.status(HttpStatus.NOT_FOUND).body(EMPTY_BODY);
        // WHEN
        var actual = exceptionHandlingAdvice.handleNoHandlerFoundException(noHandlerFoundException, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND, webRequest);
        // THEN
        verifyNoInteractions(webRequest);
        assertEquals(actual, expected);
    }
}
