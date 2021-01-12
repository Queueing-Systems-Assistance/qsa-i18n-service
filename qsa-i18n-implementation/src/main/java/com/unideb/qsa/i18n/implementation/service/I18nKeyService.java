package com.unideb.qsa.i18n.implementation.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.domain.exception.QSAClientException;
import com.unideb.qsa.i18n.implementation.resolver.I18nKeyResolver;

/**
 * Service for resolving i18n keys.
 */
@Component
public class I18nKeyService {

    private static final String ERROR_MISSING_KEYS = "Missing i18n keys for %s";

    private final I18nKeyResolver i18NKeyResolver;

    public I18nKeyService(I18nKeyResolver i18NKeyResolver) {
        this.i18NKeyResolver = i18NKeyResolver;
    }

    /**
     * Get the given i18n key values. It will resolve all the locales for an i18n key.
     * @param i18nKeys list of the keys
     * @return resolved {@link I18nElement} keys.
     */
    public Collection<I18nElement> getI18nKeys(Collection<String> i18nKeys) {
        Map<String, I18nElement> resolvedI18nKeys = resolveI18nKeys(i18nKeys);
        isI18nKeyValid(resolvedI18nKeys);
        return resolvedI18nKeys.values();
    }

    private Map<String, I18nElement> resolveI18nKeys(Collection<String> i18nKeys) {
        return i18nKeys.stream().collect(HashMap::new, (map, key) -> map.put(key, i18NKeyResolver.resolveI18nKey(key)), HashMap::putAll);
    }


    private void isI18nKeyValid(Map<String, I18nElement> i18nElements) {
        List<String> invalidI18nKeys = i18nElements.keySet().stream()
                                                   .filter(key -> Objects.isNull(i18nElements.get(key)))
                                                   .collect(Collectors.toList());
        if (!invalidI18nKeys.isEmpty()) {
            throw new QSAClientException(String.format(ERROR_MISSING_KEYS, invalidI18nKeys));
        }
    }
}
