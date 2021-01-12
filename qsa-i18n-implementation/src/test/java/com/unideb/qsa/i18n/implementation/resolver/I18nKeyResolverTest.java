package com.unideb.qsa.i18n.implementation.resolver;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.testng.Assert.assertEquals;

import java.util.Map;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.unideb.qsa.i18n.domain.I18nElement;

/**
 * Unit tests for {@link I18nKeyResolver}.
 */
public class I18nKeyResolverTest {

    private static final String I18N_TEST_KEY = "test.key";
    private static final I18nElement I18N_ELEMENT = new I18nElement(I18N_TEST_KEY);
    private static final Map<String, String> I18N_KEY_VALUE = Map.of("en_US", "Test value");

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private I18nKeyResolver i18nKeyResolver;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        i18nKeyResolver = new I18nKeyResolver(dynamoDBMapper);
    }

    @Test
    public void resolveI18nKey() {
        // GIVEN
        I18nElement expected = createExpectedI18nElement();
        given(dynamoDBMapper.load(I18N_ELEMENT)).willReturn(expected);
        // WHEN
        I18nElement actual = i18nKeyResolver.resolveI18nKey(I18N_TEST_KEY);
        // THEN
        verify(dynamoDBMapper).load(I18N_ELEMENT);
        assertEquals(actual, expected);
    }

    private I18nElement createExpectedI18nElement() {
        var i18nElement = new I18nElement(I18N_TEST_KEY);
        i18nElement.setValue(I18N_KEY_VALUE);
        return i18nElement;
    }
}
