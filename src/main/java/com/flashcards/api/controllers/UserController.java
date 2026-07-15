package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.RegisterWithCodeDTO;
import com.flashcards.api.dtos.request.UserRegistrationDTO;
import com.flashcards.api.dtos.response.ProfileResponseDTO;
import com.flashcards.api.dtos.response.UserResponseDTO;
import com.flashcards.api.entities.User;
import com.flashcards.api.enums.VerificationType;
import com.flashcards.api.services.ProfileService;
import com.flashcards.api.services.UserService;
import com.flashcards.api.services.VerificationService;
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

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid RegisterWithCodeDTO dto) {

        verificationService.validateCode(
                dto.email(),
                dto.code(),
                VerificationType.REGISTER
        );

        UserRegistrationDTO registrationDTO = new UserRegistrationDTO(
                dto.name(),
                dto.email(),
                dto.password()
        );

        User registeredUser = userService.registerUser(registrationDTO);

        verificationService.markAsUsed(
                dto.email(),
                dto.code(),
                VerificationType.REGISTER
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new UserResponseDTO(registeredUser));
    }

    @GetMapping("/profile/{userId}")
    public ResponseEntity<ProfileResponseDTO> getProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(profileService.getProfile(userId));
    }


}
