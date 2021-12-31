package com.unideb.qsa.i18n.retriever;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.domain.exception.QSAClientException;

/**
 * Unit tests for {@link I18nKeyRetriever}.
 */
public class I18nKeyResolverTest {

    private static final String I18N_TEST_KEY = "test.key";
    private static final String I18N_INVALID_KEY = "invalid.key";
    private static final I18nElement VALID_I18N_ELEMENT = new I18nElement(I18N_TEST_KEY);
    private static final Map<String, List<Object>> I18N_ELEMENTS_CORRECTLY = Map.of("i18n", List.of(VALID_I18N_ELEMENT));
    private static final I18nElement INVALID_I18N_ELEMENT = new I18nElement(I18N_INVALID_KEY);

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private I18nKeyRetriever i18nKeyRetriever;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        i18nKeyRetriever = new I18nKeyRetriever(dynamoDBMapper);
    }

    @Test
    public void getI18nKeysShouldReturnI18nKeys() {
        // GIVEN
        Collection<I18nElement> expected = List.of(VALID_I18N_ELEMENT);
        given(dynamoDBMapper.batchLoad(List.of(VALID_I18N_ELEMENT))).willReturn(I18N_ELEMENTS_CORRECTLY);
        // WHEN
        Collection<I18nElement> actual = i18nKeyRetriever.resolveI18nKeys(List.of(I18N_TEST_KEY));
        // THEN
        verify(dynamoDBMapper).batchLoad(List.of(VALID_I18N_ELEMENT));
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = QSAClientException.class)
    public void getI18nKeysShouldThrowExceptionWhenKeyCannotResolved() {
        // GIVEN
        given(dynamoDBMapper.batchLoad(List.of(VALID_I18N_ELEMENT, INVALID_I18N_ELEMENT))).willReturn(I18N_ELEMENTS_CORRECTLY);
        // WHEN
        i18nKeyRetriever.resolveI18nKeys(List.of(I18N_TEST_KEY, I18N_INVALID_KEY));
        // THEN
    }
}
