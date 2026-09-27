package com.flashcards.api.config;

import com.flashcards.api.dtos.request.UserRegistrationDTO;
import com.flashcards.api.entities.User;
import com.flashcards.api.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DatabaseSeederConfig {
    @Bean
    CommandLineRunner initDatabase(UserRepository repository, PasswordEncoder passwordEncoder){
        return args -> {
            if(repository.findByEmail("admin@email.com").isEmpty()){
                String passwordHash = passwordEncoder.encode("123456");
                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@email.com");
                admin.setPassword(passwordHash);
                repository.save(admin);
            }
        };
    }
}
