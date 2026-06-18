package com.flashcards.api.dtos.response;


public record LoginResponseDTO(
        String token,
        String tokenType
) {
    public LoginResponseDTO(String token) {
        this(token, "Bearer");
    }
}
