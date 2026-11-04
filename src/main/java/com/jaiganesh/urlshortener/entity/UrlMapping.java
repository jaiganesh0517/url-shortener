package com.jaiganesh.urlshortener.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name ="url_mapping")
public class UrlMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "short_code", unique = true, length = 20)
	private String shortCode;
	
	@Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
	private String originalUrl;
	
	@Column(name = "click_count")
	private Long clickCount = 0L;
	
	@Column(name ="created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
	
	@Column(name ="expires_at")
	private LocalDateTime expiresAt;
	
	public UrlMapping() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public String getOriginalUrl() {
		return originalUrl;
	}

	public void setOriginalUrl(String originalUrl) {
		this.originalUrl = originalUrl;
	}

	public Long getClickCount() {
		return clickCount;
	}

	public void setClickCount(Long clickCount) {
		this.clickCount = clickCount;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}
	
	
}
