package com.bharath.url;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class ShortnerApplication {

	public static void main(String[] args) {
		 try {
	            Dotenv dotenv = Dotenv.configure()
	                .ignoreIfMissing() // Gracefully handle missing .env file
	                .filename(".env") // The name of the .env file
	                .load(); // Load the environment variables from the .env file
	            
	            // Set .env variables as system properties if available
	            dotenv.entries().forEach(entry -> 
	                System.setProperty(entry.getKey(), entry.getValue())
	            );
	        } catch (Exception e) {
	            // If .env is missing, it will be ignored without causing an exception
	            System.out.println("Warning: .env file not found or failed to load.");
	        }
		SpringApplication.run(ShortnerApplication.class, args);
	}

}
