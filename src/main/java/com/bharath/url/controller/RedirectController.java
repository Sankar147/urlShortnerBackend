package com.bharath.url.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bharath.url.models.UrlMapping;
import com.bharath.url.service.UrlMappingService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RedirectController {

	private UrlMappingService urlMappingService;
	
//	 @GetMapping("/{shortUrl}")
//	    public ResponseEntity<Void> redirect(@PathVariable String shortUrl){
//	        UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);
//	        if (urlMapping != null) {
//	            HttpHeaders httpHeaders = new HttpHeaders(null);
//	            httpHeaders.add("Location", urlMapping.getOriginalUrl());
//	            return ResponseEntity.status(302).headers(httpHeaders).build();
//	        } else {
//	            return ResponseEntity.notFound().build();
//	        }
//	    }
	@GetMapping("/{shortUrl}")
	public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
	    UrlMapping urlMapping = urlMappingService.getOriginalUrl(shortUrl);

	    if (urlMapping == null || urlMapping.getOriginalUrl() == null) {
	        return ResponseEntity.notFound().build();
	    }

	    HttpHeaders httpHeaders = new HttpHeaders();
	    httpHeaders.setLocation(URI.create(urlMapping.getOriginalUrl())); // More reliable than add()

	    return ResponseEntity.status(HttpStatus.FOUND).headers(httpHeaders).build();
	}

}
