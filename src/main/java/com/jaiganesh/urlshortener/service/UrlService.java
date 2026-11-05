package com.jaiganesh.urlshortener.service;

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
	public UrlService(UrlMappingRepository urlMappingRepository, Base62Service base62Service,
			AppProperties appProperties) {
		super();
		this.urlMappingRepository = urlMappingRepository;
		this.base62Service = base62Service;
		this.appProperties = appProperties;
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
		
		return new ShortenResponse(
				shortUrl,
				shortCode,
				originalUrl,
				urlMapping.getCreatedAt()
		);
	}
	
	public String getOriginalUrl(String shortCode) {
		
		UrlMapping urlMapping = urlMappingRepository.findByShortCode(shortCode)
				     .orElseThrow(() -> new UrlNotFoundException("Short URL not found" + shortCode));
		
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

}
