package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.LoginRequestDTO;
import com.flashcards.api.dtos.response.LoginResponseDTO;
import com.flashcards.api.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO dto) {
        String jwtToken = authService.authenticate(dto);
        return ResponseEntity.ok(new LoginResponseDTO(jwtToken));
    }
}
