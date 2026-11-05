package com.jaiganesh.urlshortener.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jaiganesh.urlshortener.service.UrlService;

@RestController
public class RedirectController {

	private final UrlService urlService;

	public RedirectController(UrlService urlService) {
		this.urlService = urlService;
	}
	
	@GetMapping("/{shortCode}")
	public ResponseEntity<Void> redirect(@PathVariable String shortCode){
		
		String originalUrl = urlService.getOriginalUrl(shortCode);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Location", originalUrl);
		
		return new ResponseEntity<>(headers, HttpStatus.FOUND);
	}
	
	
}
