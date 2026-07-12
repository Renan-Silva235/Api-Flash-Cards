package com.flashcards.api.dtos.response;

public record ProfileResponseDTO(
        String name,
        String email,
        long totalDecks,
        long totalFlashcards,
        long favoriteDecks
) {
}