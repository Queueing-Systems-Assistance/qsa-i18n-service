package com.unideb.qsa.i18n.server.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.implementation.updater.I18nKeyUpdater;

/**
 * Controller to update i18n keys.
 */
@RestController
public class I18nKeyUpdateController {

    private final I18nKeyUpdater i18NKeyUpdater;

    public I18nKeyUpdateController(I18nKeyUpdater i18NKeyUpdater) {
        this.i18NKeyUpdater = i18NKeyUpdater;
    }

    /**
     * Update i18n key values.
     * @param keys list of i18n keys with it's values
     */
    @PostMapping("keys/update")
    public void update(@RequestBody List<I18nElement> keys) {
        i18NKeyUpdater.update(keys);
    }
}
