package org.xiaowu.behappy.screw.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xiaowu.behappy.screw.enums.CacheEnum;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xiaowu
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(value = "spring.cache.type", havingValue = "caffeine")
public class CaffeineConfig {

    @Bean
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager simpleCacheManager = new SimpleCacheManager();
        List<CaffeineCache> caches = new ArrayList<>();

        for (CacheEnum value : CacheEnum.values()) {
            Cache<Object, Object> cache = Caffeine.newBuilder()
                    .initialCapacity(value.getInitialCapacity())
                    .maximumSize(value.getMaximumSize())
                    //写入后失效时间
                    .expireAfterWrite(Duration.ofSeconds(value.getExpire()))
                    .build();
            caches.add(new CaffeineCache(value.name(), cache));
        }
        simpleCacheManager.setCaches(caches);

        return simpleCacheManager;
    }
}
