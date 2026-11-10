package com.jaiganesh.urlshortener.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jaiganesh.urlshortener.service.RateLimitService;
import com.jaiganesh.urlshortener.service.UrlService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
public class RedirectController {

	private final UrlService urlService;
	private final RateLimitService rateLimitService;

	public RedirectController(UrlService urlService, RateLimitService rateLimitService) {
		this.urlService = urlService;
		this.rateLimitService = rateLimitService;
	}
	
	@GetMapping("/{shortCode}")
	public ResponseEntity<Void> redirect(@PathVariable String shortCode , HttpServletRequest httpRequest){
		
		if ("favicon.ico".equals(shortCode)) {
	        return ResponseEntity.notFound().build();
	    }
		String originalUrl = urlService.getOriginalUrl(shortCode);
		
		String clientIp = getClientIp(httpRequest);
        rateLimitService.checkRateLimit(clientIp); 
        
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", originalUrl);
		
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
	private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
	
}
