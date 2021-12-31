package com.unideb.qsa.i18n.retriever;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.unideb.qsa.i18n.domain.I18nElement;
import com.unideb.qsa.i18n.domain.exception.QSAClientException;

/**
 * Service for resolving i18n keys.
 */
public final class I18nKeyRetriever {

    private static final String ERROR_MISSING_KEYS = "Missing i18n key for values for %s";
    private static final String DATABASE_NAME_I18N = "i18n";
    private static final I18nElement I18N_NULL_MISSING_ELEMENT = null;

    private final DynamoDBMapper dynamoDBMapper;

    public I18nKeyRetriever(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Resolved i18n keys from the DynamoDB.
     * @param i18nKeys list of i18n keys
     * @return a collection with the resolved elements. If no i18n keys found an empty list will be returned
     */
    public Collection<I18nElement> resolveI18nKeys(Collection<String> i18nKeys) {
        Map<String, I18nElement> resolvedI18nKeys = readElements(i18nKeys);
        validateI18nKeys(resolvedI18nKeys);
        return resolvedI18nKeys.values();
    }

    private Map<String, I18nElement> readElements(Collection<String> i18nKeys) {
        List<I18nElement> itemsToResolve = convertItems(i18nKeys);
        Map<String, List<Object>> resolvedItems = dynamoDBMapper.batchLoad(itemsToResolve);
        return i18nKeys.stream().collect(HashMap::new, (map, key) -> map.put(key, getI18nKey(resolvedItems, key)), HashMap::putAll);
    }

    private I18nElement getI18nKey(Map<String, List<Object>> resolvedItems, String key) {
        return resolvedItems.get(DATABASE_NAME_I18N).stream()
                            .map(object -> (I18nElement) object)
                            .filter(i18nElement -> i18nElement.getKey().equals(key))
                            .findFirst()
                            .orElse(I18N_NULL_MISSING_ELEMENT);
    }

    private List<I18nElement> convertItems(Collection<String> i18nKeys) {
        return i18nKeys.stream()
                       .map(I18nElement::new)
                       .collect(Collectors.toList());
    }

    private void validateI18nKeys(Map<String, I18nElement> i18nElements) {
        List<String> invalidI18nKeys = getInvalidI18nKeys(i18nElements);
        if (!invalidI18nKeys.isEmpty()) {
            throw new QSAClientException(String.format(ERROR_MISSING_KEYS, invalidI18nKeys));
        }
    }

    private List<String> getInvalidI18nKeys(Map<String, I18nElement> i18nElements) {
        return i18nElements.keySet().stream()
                           .filter(key -> Objects.isNull(i18nElements.get(key)))
                           .collect(Collectors.toList());
    }
}
