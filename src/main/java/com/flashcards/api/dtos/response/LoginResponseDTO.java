package com.flashcards.api.dtos.response;

public record LoginResponseDTO(
        String token,
        String tokenType,
        UserResponseDTO user
) {
    public LoginResponseDTO(String token, String tokenType, UserResponseDTO user) {
        this.token = token;
        this.tokenType = tokenType;
        this.user = user;
    }
}