package com.flashcards.api.services;

import com.flashcards.api.dtos.request.LoginRequestDTO;
import com.flashcards.api.exceptions.UnauthorizedException;
import com.flashcards.api.security.jwt.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    public String authenticate(LoginRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password())
            );
            return jwtService.generateToken(authentication);
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("E-mail ou senha incorretos.");
        }
    }
}