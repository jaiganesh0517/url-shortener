package com.jaiganesh.urlshortener.dto;

import java.time.LocalDateTime;

public class ShortenResponse {
	
	private String shortUrl;
	private String shortCode;
	private String originalUrl;
	private LocalDateTime createdAt;
	
	public ShortenResponse(String shortUrl, String shortCode, String originalUrl, LocalDateTime createdAt) {
		this.shortUrl = shortUrl;
		this.shortCode = shortCode;
		this.originalUrl = originalUrl;
		this.createdAt = createdAt;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public String getShortCode() {
		return shortCode;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	
	

}
