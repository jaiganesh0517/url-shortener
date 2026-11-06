package com.jaiganesh.urlshortener.service;

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
	public UrlService(UrlMappingRepository urlMappingRepository, Base62Service base62Service,
			AppProperties appProperties, RedisTemplate<String, String> redisTemplate) {
		super();
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
		
		urlMapping = urlMappingRepository.save(urlMapping);
		
		String shortCode = base62Service.encode(urlMapping.getId());
		
		urlMapping.setShortCode(shortCode);
		urlMappingRepository.save(urlMapping);
		
		String shortUrl = appProperties.getBaseUrl() + "/" + shortCode;
		redisTemplate.opsForValue().set(shortUrl, originalUrl, java.time.Duration.ofHours(24));
		
		return new ShortenResponse(
				shortUrl,
				shortCode,
				originalUrl,
				urlMapping.getCreatedAt()
		);
	}
	
	public String getOriginalUrl(String shortCode) {
		String cachedUrl = redisTemplate.opsForValue().get(shortCode);
		if(cachedUrl != null) {
			increaseClickCount(shortCode);
			return cachedUrl;
		}
		
		UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
				     .orElseThrow(() -> new UrlNotFoundException("Short URL not found" + shortCode));
		
		redisTemplate.opsForValue().set(shortCode, urlMapping.getOriginalUrl(), java.time.Duration.ofHours(24));
		urlMapping.setClickCount(urlMapping.getClickCount() + 1);
		urlMappingRepository.save(urlMapping);
		
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
