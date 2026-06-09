package com.flashcards.api.dtos.response;

import com.flashcards.api.entities.User;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String name,
        String email
) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getEmail());
    }
}