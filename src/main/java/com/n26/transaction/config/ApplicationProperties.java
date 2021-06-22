package com.n26.transaction.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@ConfigurationProperties
public class ApplicationProperties {

        @Value("${transaction.persist.duration:60}")
        private long transactionPersistDuration;

        @Value("${transaction.cache.eviction.time:12000}")
        private long cacheEvictionTime;
}
