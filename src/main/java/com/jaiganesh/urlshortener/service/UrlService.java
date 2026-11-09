package com.jaiganesh.urlshortener.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaiganesh.urlshortener.config.AppProperties;
import com.jaiganesh.urlshortener.dto.ShortenResponse;
import com.jaiganesh.urlshortener.dto.StatsResponse;
import com.jaiganesh.urlshortener.entity.UrlMapping;
import com.jaiganesh.urlshortener.exception.UrlNotFoundException;
import com.jaiganesh.urlshortener.repository.UrlMappingRepository;

@Service
public class UrlService {

    private final UrlMappingRepository urlMappingRepository;
    private final Base62Service base62Service;
    private final AppProperties appProperties;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String CACHE_PREFIX = "url:";
    private static final Duration CACHE_TTL = Duration.ofHours(24);

    public UrlService(UrlMappingRepository urlMappingRepository,
                      Base62Service base62Service,
                      AppProperties appProperties,
                      RedisTemplate<String, String> redisTemplate) {
        this.urlMappingRepository = urlMappingRepository;
        this.base62Service = base62Service;
        this.appProperties = appProperties;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public ShortenResponse createShortUrl(String originalUrl) {
        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setClickCount(0L);

        urlMapping = urlMappingRepository.save(urlMapping);          // get ID

        String shortCode = base62Service.encode(urlMapping.getId());
        urlMapping.setShortCode(shortCode);
        urlMappingRepository.save(urlMapping);

        
        String cacheKey = CACHE_PREFIX + shortCode;
        redisTemplate.opsForValue().set(cacheKey, originalUrl, CACHE_TTL);

        String shortUrl = appProperties.getBaseUrl() + "/" + shortCode;

        return new ShortenResponse(shortUrl, shortCode, originalUrl, urlMapping.getCreatedAt());
    }

    public String getOriginalUrl(String shortCode) {
        String cacheKey = CACHE_PREFIX + shortCode;

    

        String cachedUrl = redisTemplate.opsForValue().get(cacheKey);

        if (cachedUrl != null) {
            
            increaseClickCount(shortCode);
            return cachedUrl;
        }

        

        UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortCode));

        redisTemplate.opsForValue().set(cacheKey, urlMapping.getOriginalUrl(), CACHE_TTL);
        increaseClickCount(shortCode);

        return urlMapping.getOriginalUrl();
    }
	
	public StatsResponse getStats(String shortCode) {
		
		UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
				.orElseThrow(() -> new UrlNotFoundException("Short URL not found for code: " + shortCode));
		
		return new StatsResponse(
				urlMapping.getShortCode(),
				urlMapping.getOriginalUrl(),
				urlMapping.getClickCount(),
				urlMapping.getCreatedAt()
				);
	}
	
	public void increaseClickCount(String shortCode) {
		urlMappingRepository.findByShortCode(shortCode).ifPresent(urlMapping -> {
			urlMapping.setClickCount(urlMapping.getClickCount() + 1);
			urlMappingRepository.save(urlMapping);
		});
	}

}
