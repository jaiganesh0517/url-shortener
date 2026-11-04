package com.jaiganesh.urlshortener.dto;

import org.hibernate.validator.constraints.URL;

import jakarta.validation.constraints.NotBlank;

public class ShortenRequest {

	@NotBlank(message = "URL is required")
	@URL(message = "Invalid URL format")
	private String originalUrl;
	
	public String getOriginalUrl() {
		return originalUrl;
	}
	
	public void setOriginalUrl(String originalUrl) {
		this.originalUrl=originalUrl;
	}
}
