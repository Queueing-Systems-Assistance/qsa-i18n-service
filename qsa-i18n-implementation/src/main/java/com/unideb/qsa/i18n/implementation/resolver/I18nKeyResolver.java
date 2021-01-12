package com.unideb.qsa.i18n.implementation.resolver;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import com.unideb.qsa.i18n.domain.I18nElement;

/**
 * Resolver for i18n key.
 */
@Component
public class I18nKeyResolver {

    private final DynamoDBMapper dynamoDBMapper;

    public I18nKeyResolver(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    /**
     * Resolves and caches an i18n key. The cache time depends on the config in the server package.
     * @param key an i18n key
     * @return an {@link I18nElement} with its locales
     */
    @Cacheable(value = "i18nKeys", unless = "#result == null")
    public I18nElement resolveI18nKey(String key) {
        return dynamoDBMapper.load(new I18nElement(key));
    }
}
