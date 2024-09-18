package com.berkinozturk.event.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    private final RedisConnectionFactory redisConnectionFactory;
    private static final Duration USERS_CACHE_TTL = Duration.ofMinutes(10);
    private static final Duration EVENTS_CACHE_TTL = Duration.ofSeconds(20);

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }

    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration userCacheConfig = cacheConfiguration()
                .entryTtl(USERS_CACHE_TTL);

        RedisCacheConfiguration eventCacheConfig = cacheConfiguration()
                .entryTtl(EVENTS_CACHE_TTL);

        return RedisCacheManager.builder(redisConnectionFactory)
                .withCacheConfiguration("users", userCacheConfig)
                .withCacheConfiguration("events", eventCacheConfig)
                .build();
    }
}
