package com.jaiganesh.urlshortener.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaiganesh.urlshortener.dto.ShortenRequest;
import com.jaiganesh.urlshortener.dto.ShortenResponse;
import com.jaiganesh.urlshortener.service.UrlService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UrlController {

    private final UrlService urlService;

    // Correct constructor
    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping("/shorten")
    public ResponseEntity<ShortenResponse> createShortUrl(
            @Valid @RequestBody ShortenRequest request) {

        ShortenResponse response = urlService.createShortUrl(request.getOriginalUrl());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}