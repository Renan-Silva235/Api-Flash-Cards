package com.flashcards.api;

import com.flashcards.api.config.DotenvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiFlashcardsApplication {
	public static void main(String[] args) {
		DotenvConfig.load();
		SpringApplication.run(ApiFlashcardsApplication.class, args);
	}

}
