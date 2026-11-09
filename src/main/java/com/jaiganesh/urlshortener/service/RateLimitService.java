package com.jaiganesh.urlshortener.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.jaiganesh.urlshortener.config.AppProperties;
import com.jaiganesh.urlshortener.exception.RateLimitExceededException;

@Service
public class RateLimitService {

    private final RedisTemplate<String, String> redisTemplate;
    private final AppProperties appProperties;

    private static final String RATE_LIMIT_PREFIX = "rate_limit:ip:";

    public RateLimitService(RedisTemplate<String, String> redisTemplate, AppProperties appProperties) {
        this.redisTemplate = redisTemplate;
        this.appProperties = appProperties;
    }

    public void checkRateLimit(String clientIp) {
        String key = RATE_LIMIT_PREFIX + clientIp;
        int maxRequests = appProperties.getRateLimit().getMaxRequests();
        int windowSeconds = appProperties.getRateLimit().getWindowSeconds();

        Long currentCount = redisTemplate.opsForValue().increment(key);

        
        if (currentCount != null && currentCount == 1) {
            redisTemplate.expire(key, Duration.ofSeconds(windowSeconds));
        }

        
        if (currentCount != null && currentCount > maxRequests) {
            throw new RateLimitExceededException(
                "Rate limit exceeded. Maximum " + maxRequests +
                " requests allowed per " + windowSeconds + " seconds."
            );
        }
    }
}