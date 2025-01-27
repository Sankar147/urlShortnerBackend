package com.bharath.url.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bharath.url.dtos.LoginRequest;
import com.bharath.url.models.User;
import com.bharath.url.repository.UserRepository;
import com.bharath.url.security.JwtAuthenticationResponse;
import com.bharath.url.security.JwtUtils;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class UserService {

	
	 private PasswordEncoder passwordEncoder;
	private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;
	
	public User registerUser(User user) {
		// TODO Auto-generated method stub
		 user.setPassword(passwordEncoder.encode(user.getPassword()));
	        return userRepository.save(user);
		
	}

	public JwtAuthenticationResponse authenticateUser(LoginRequest loginRequest) {
		 Authentication authentication = authenticationManager.authenticate(
	                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
	                        loginRequest.getPassword()));
	        SecurityContextHolder.getContext().setAuthentication(authentication);
	        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	        String jwt = jwtUtils.generateToken(userDetails);
	        return new JwtAuthenticationResponse(jwt);
	}

	public User findByUsername(String name) {
		// TODO Auto-generated method stub
		 return userRepository.findByUsername(name).orElseThrow(
	                () -> new UsernameNotFoundException("User not found with username: " + name)
	        );
	}

}
