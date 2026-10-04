package com.flashcards.api.security;

import com.flashcards.api.entities.User;
import com.flashcards.api.exceptions.UnauthorizedException;
import com.flashcards.api.security.userDetails.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Fonte única do usuário logado: vem do JWT validado pelo JwtAuthenticationFilter.
 * Nunca confie em ids de usuário enviados pelo cliente (URL ou corpo da requisição),
 * eles podem ser alterados por qualquer pessoa.
 */
@Component
public class CurrentUserService {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new UnauthorizedException("Usuário não autenticado.");
        }

        return userDetails.getUser();
    }

    public UUID getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
