package com.unideb.qsa.i18n.implementation.service;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

import java.util.Collection;
import java.util.List;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.domain.exception.QSAClientException;
import com.unideb.qsa.i18n.implementation.resolver.I18nKeyResolver;

/**
 * Unit tests for {@link I18nKeyService}.
 */
public class I18nKeyServiceTest {

    private static final String I18N_TEST_KEY = "test.key";
    private static final String I18N_INVALID_KEY = "invalid.key";
    private static final I18nElement I18N_ELEMENT = new I18nElement(I18N_TEST_KEY);
    private static final I18nElement NULL_I18N_ELEMENT = null;

    @Mock
    private I18nKeyResolver i18nKeyResolver;

    private I18nKeyService i18nKeyService;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        i18nKeyService = new I18nKeyService(i18nKeyResolver);
    }

    @Test
    public void getI18nKeysShouldReturnI18nKeys() {
        // GIVEN
        Collection<I18nElement> expected = List.of(I18N_ELEMENT);
        given(i18nKeyResolver.resolveI18nKey(I18N_TEST_KEY)).willReturn(I18N_ELEMENT);
        // WHEN
        Collection<I18nElement> actual = i18nKeyService.getI18nKeys(List.of(I18N_TEST_KEY));
        // THEN
        verify(i18nKeyResolver).resolveI18nKey(I18N_TEST_KEY);
        assertEquals(actual, expected);
    }

    @Test(expectedExceptions = QSAClientException.class)
    public void getI18nKeysShouldThrowExceptionWhenKeyCannotResolved() {
        // GIVEN
        given(i18nKeyResolver.resolveI18nKey(I18N_TEST_KEY)).willReturn(I18N_ELEMENT);
        given(i18nKeyResolver.resolveI18nKey(I18N_INVALID_KEY)).willReturn(NULL_I18N_ELEMENT);
        // WHEN
        i18nKeyService.getI18nKeys(List.of(I18N_TEST_KEY, I18N_INVALID_KEY));
        // THEN
    }
}
