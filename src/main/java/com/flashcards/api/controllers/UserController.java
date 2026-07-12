package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.UserRegistrationDTO;
import com.flashcards.api.dtos.response.ProfileResponseDTO;
import com.flashcards.api.dtos.response.UserResponseDTO;
import com.flashcards.api.entities.User;
import com.flashcards.api.services.ProfileService;
import com.flashcards.api.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegistrationDTO dto) {
        User registeredUser = userService.registerUser(dto);
        UserResponseDTO response = new UserResponseDTO(registeredUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }


}
