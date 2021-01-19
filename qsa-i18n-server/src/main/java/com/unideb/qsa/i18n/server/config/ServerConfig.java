package com.unideb.qsa.i18n.server.config;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.annotation.Scheduled;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;

/**
 * Configuration for the server.
 */
@Configuration
public class ServerConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ServerConfig.class);
    private static final int REFRESH_RATE = 1000 * 60 * 60 * 12;

    private final BuildProperties buildProperties;

    @Value("${management.metrics.enabled}")
    private String[] enabledMetrics;
    @Value("${aws.access-key-id}")
    private String awsAccessKeyId;
    @Value("${aws.secret-access-key}")
    private String awsSecretAccessKey;

    public ServerConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean(name = "i18nKeys")
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    @Scheduled(fixedRate = REFRESH_RATE)
    @CacheEvict(allEntries = true, value = "i18nKeys")
    public void reportCacheEvict() {
        LOG.info("Refreshing i18n keys");
    }

    @PostConstruct
    public void setup() {
        String applicationVersion = buildProperties.getVersion();
        LOG.info("Setting [{}] application version to logback", applicationVersion);
        MDC.put("version", applicationVersion);
    }

    @Bean
    public MeterFilter meterFilter() {
        return new MeterFilter() {
            @Override
            @NonNull
            public MeterFilterReply accept(@NonNull Meter.Id id) {
                return Optional.of(List.of(enabledMetrics).contains(id.getName()))
                               .filter(isEnabled -> isEnabled)
                               .map(isEnabled -> MeterFilterReply.ACCEPT)
                               .orElse(MeterFilterReply.DENY);
            }
        };
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper() {
        return new DynamoDBMapper(AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.EU_CENTRAL_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey)))
                .build());
    }
}
