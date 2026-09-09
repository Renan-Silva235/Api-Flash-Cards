package com.flashcards.api.dtos.response;

public record LoginResponseDTO(
        String token,
        String tokenType,
        UserResponseDTO user
) {}

