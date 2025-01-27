package com.bharath.url.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bharath.url.models.User;

@Repository
public interface UserRepository  extends JpaRepository<User,Long>{

	 Optional<User> findByUsername(String username);
	
	
}

