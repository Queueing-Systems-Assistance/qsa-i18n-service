package com.unideb.qsa.i18n.server.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

import java.util.List;

import org.mockito.Mock;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.implementation.updater.I18nKeyUpdater;

/**
 * Unit tests for {@link I18nKeyUpdateController}.
 */
public class I18nKeyUpdateControllerTest {

    @Mock
    private I18nKeyUpdater i18NKeyUpdater;

    private I18nKeyUpdateController i18nKeyUpdateController;

    @BeforeMethod
    public void setup() {
        openMocks(this);
        i18nKeyUpdateController = new I18nKeyUpdateController(i18NKeyUpdater);
    }

    @Test
    public void update() {
        // GIVEN
        var i18nElements = List.of(new I18nElement("simple.key"));
        doNothing().when(i18NKeyUpdater).update(i18nElements);
        // WHEN
        i18nKeyUpdateController.update(i18nElements);
        // THEN
        verify(i18NKeyUpdater).update(i18nElements);
    }
}
