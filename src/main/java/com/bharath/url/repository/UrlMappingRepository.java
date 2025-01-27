package com.bharath.url.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bharath.url.models.UrlMapping;
import com.bharath.url.models.User;

@Repository
public interface UrlMappingRepository extends JpaRepository<UrlMapping, Long>{

	 UrlMapping findByShortUrl(String shortUrl);
	   List<UrlMapping> findByUser(User user);
}
