package com.unideb.qsa.i18n.server.controller;

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
import com.unideb.qsa.i18n.implementation.service.I18nKeyService;

/**
 * Unit tests for {@link I18nKeyController}.
 */
public class I18nKeyControllerTest {

    private static final List<String> KEYS = List.of("test.key", "another.test.key");

    @Mock
    private I18nKeyService i18nKeyService;
    private I18nKeyController i18nKeyController;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        i18nKeyController = new I18nKeyController(i18nKeyService);
    }

    @Test
    public void get() {
        // GIVEN
        var expected = List.of(new I18nElement("test.key"), new I18nElement("another.test.key"));
        given(i18nKeyService.getI18nKeys(KEYS)).willReturn(expected);
        // WHEN
        Collection<I18nElement> actual = i18nKeyController.get(KEYS);
        // THEN
        verify(i18nKeyService).getI18nKeys(KEYS);
        assertEquals(actual, expected);
    }
}
