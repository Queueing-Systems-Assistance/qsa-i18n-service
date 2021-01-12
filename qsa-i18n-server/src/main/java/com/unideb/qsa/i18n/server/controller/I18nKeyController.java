package com.unideb.qsa.i18n.server.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.implementation.service.I18nKeyService;

/**
 * Controller for i18n keys.
 */
@RestController
public class I18nKeyController {

    private final I18nKeyService i18nKeyService;

    public I18nKeyController(I18nKeyService i18nKeyService) {
        this.i18nKeyService = i18nKeyService;
    }

    /**
     * Get i18n key values. It'll resolve all the available locale values as well.
     * @param keys list of i18n keys
     * @return resolved {@link I18nElement} keys. It is guaranteed, that it will return all the requested i18n keys.
     */
    @PostMapping("keys")
    public Collection<I18nElement> get(@RequestBody List<String> keys) {
        return i18nKeyService.getI18nKeys(keys);
    }
}
