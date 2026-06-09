package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.UserRegistrationDTO;
import com.flashcards.api.dtos.response.UserResponseDTO;
import com.flashcards.api.entities.User;
import com.flashcards.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegistrationDTO dto) {
        User registeredUser = userService.registerUser(dto);
        UserResponseDTO response = new UserResponseDTO(registeredUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
