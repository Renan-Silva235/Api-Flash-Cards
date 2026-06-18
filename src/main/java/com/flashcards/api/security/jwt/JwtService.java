package com.flashcards.api.security.jwt;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.flashcards.api.exceptions.UnauthorizedException;
import com.flashcards.api.security.userDetails.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class JwtService {

    @Value("${JWT_SECRET}")
    private String secret;

    public String generateToken(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return com.auth0.jwt.JWT.create()
                    .withIssuer("flashcards-api")
                    .withSubject(userDetails.getUsername())
                    .withClaim("id", userDetails.getUser().getId().toString())
                    .withExpiresAt(getExpirationDate())
                    .sign(algorithm);
        } catch (Exception exception) {
            throw new RuntimeException("Erro ao processar e criar token de segurança.", exception);
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return com.auth0.jwt.JWT.require(algorithm)
                    .withIssuer("flashcards-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new UnauthorizedException("Token inválido ou expirado.");
        } catch (Exception exception) {
            throw new RuntimeException("Erro interno ao validar o token.", exception);
        }
    }

    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(3).toInstant(ZoneOffset.of("-03:00"));
    }
}
