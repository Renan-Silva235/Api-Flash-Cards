package com.flashcards.api.services;

import com.flashcards.api.dtos.request.UserRegistrationDTO;
import com.flashcards.api.entities.User;
import com.flashcards.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(UserRegistrationDTO dto) {
        if (userRepository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado.");
        }
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        String encryptedPassword = passwordEncoder.encode(dto.password());
        user.setPassword(encryptedPassword);
        return userRepository.save(user);
    }

    public void changePassword(String email, String newPassword) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void validateEmailAvailability(String email) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Este e-mail já está cadastrado.");
        }
    }

    public void validateEmailExists(String email) {

        if (userRepository.findByEmail(email).isEmpty()) {
            throw new RuntimeException("Não foi encontrado nenhum usuário com este e-mail.");
        }
    }
}