package com.bharath.url.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bharath.url.dtos.ClickEventDTO;
import com.bharath.url.dtos.UrlMappingDTO;
import com.bharath.url.models.ClickEvent;
import com.bharath.url.models.UrlMapping;
import com.bharath.url.models.User;
import com.bharath.url.repository.ClickEventRepository;
import com.bharath.url.repository.UrlMappingRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UrlMappingService {
	  private UrlMappingRepository urlMappingRepository;
	  private ClickEventRepository clickEventRepository;
	
	public UrlMappingDTO createShortUrl(String originalUrl, User user) {
		// TODO Auto-generated method stub
		 String shortUrl = generateShortUrl();
	        UrlMapping urlMapping = new UrlMapping();
	        urlMapping.setOriginalUrl(originalUrl);
	        urlMapping.setShortUrl(shortUrl);
	        urlMapping.setUser(user);
	        urlMapping.setCreatedDate(LocalDateTime.now());
	        UrlMapping savedUrlMapping = urlMappingRepository.save(urlMapping);
	        return convertToDto(savedUrlMapping);
	}

	private UrlMappingDTO convertToDto(UrlMapping urlMapping) {
		// TODO Auto-generated method stub
		 UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
	        urlMappingDTO.setId(urlMapping.getId());
	        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
	        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
	        urlMappingDTO.setClickCount(urlMapping.getClickCount());
	        urlMappingDTO.setCreatedDate(urlMapping.getCreatedDate());
	        urlMappingDTO.setUsername(urlMapping.getUser().getUsername());
	        return urlMappingDTO;
	}

	private String generateShortUrl() {
		// TODO Auto-generated method stub
		 String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	        Random random = new Random();
	        StringBuilder shortUrl = new StringBuilder(8);

	        for (int i = 0; i < 8; i++) {
	            shortUrl.append(characters.charAt(random.nextInt(characters.length())));
	        }
	        return shortUrl.toString();
	}

	public List<UrlMappingDTO> getUrlsByUser(User user) {
		// TODO Auto-generated method stub
		  return urlMappingRepository.findByUser(user).stream()
	                .map(this::convertToDto)
	                .toList();
	}

	public List<ClickEventDTO> getClickEventsByDate(String shortUrl, LocalDateTime start, LocalDateTime end) {
		// TODO Auto-generated method stub
			UrlMapping  urlMapping=urlMappingRepository.findByShortUrl(shortUrl);
			if(urlMapping!=null) {
				return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream()
						.collect(Collectors.groupingBy(click->click.getClickDate().toLocalDate(), Collectors.counting()))
						.entrySet().stream()
						.map(entry->{
							ClickEventDTO clickEventDTO =new ClickEventDTO();
							 clickEventDTO.setClickDate(entry.getKey());
		                        clickEventDTO.setCount(entry.getValue());
		                        return clickEventDTO;
							
						})
						.collect(Collectors.toList());
			}
			return null;
	}

	public Map<LocalDate, Long> getTotalClicksByUserAndDate(User user, LocalDate start, LocalDate end) {
		// TODO Auto-generated method stub
		  List<UrlMapping> urlMappings = urlMappingRepository.findByUser(user);
	        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMappings, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
	        return clickEvents.stream()
	                .collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));

	}
	
	  public UrlMapping getOriginalUrl(String shortUrl) {
	        UrlMapping urlMapping = urlMappingRepository.findByShortUrl(shortUrl);
	        if (urlMapping != null) {
	            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
	            urlMappingRepository.save(urlMapping);

	            // Record Click Event
	            ClickEvent clickEvent = new ClickEvent();
	            clickEvent.setClickDate(LocalDateTime.now());
	            clickEvent.setUrlMapping(urlMapping);
	            clickEventRepository.save(clickEvent);
	        }

	        return urlMapping;
	    }
}

