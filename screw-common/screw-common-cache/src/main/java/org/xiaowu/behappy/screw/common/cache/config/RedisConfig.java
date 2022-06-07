package org.xiaowu.behappy.screw.common.cache.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.xiaowu.behappy.screw.common.cache.enums.CacheEnum;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * redis config
 * @author xiaowu
 */
@Configuration
@ConditionalOnProperty(value = "spring.cache.type",havingValue = "redis")
@EnableCaching
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    @Bean
    public RedisSerializer<String> redisKeySerializer() {
        return RedisSerializer.string();
    }

    @Bean
    public RedisSerializer<Object> redisValueSerializer() {
        return RedisSerializer.json();
    }

    @Bean(name = "cacheManager")
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory
            , RedisSerializer<String> redisKeySerializer, RedisSerializer<Object> redisValueSerializer) {
        RedisCacheConfiguration difConf = getDefConf(redisKeySerializer, redisValueSerializer).entryTtl(Duration.ofHours(1));

        //自定义的缓存过期时间配置
        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        for (CacheEnum cacheEnum : CacheEnum.values()) {
            RedisCacheConfiguration conf = getDefConf(redisKeySerializer, redisValueSerializer).entryTtl(Duration.ofSeconds(cacheEnum.getExpire()));
            redisCacheConfigurationMap.put(cacheEnum.name(), conf);
        }
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(difConf)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, objects) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(":" + method.getName() + ":");
            for (Object obj : objects) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    private RedisCacheConfiguration getDefConf(RedisSerializer<String> redisKeySerializer, RedisSerializer<Object> redisValueSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith(cacheName -> "cache".concat(":").concat(cacheName).concat(":"))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisKeySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer));
    }
}
