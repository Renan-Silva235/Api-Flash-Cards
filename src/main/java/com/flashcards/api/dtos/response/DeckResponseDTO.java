package com.flashcards.api.dtos.response;

import com.flashcards.api.entities.Deck;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeckResponseDTO(
        UUID id,
        String name,
        String language,
        String category,
        Boolean favorite,
        UUID userId,
        Integer cardsCount,
        LocalDateTime createdAt
) {
    public DeckResponseDTO(Deck deck) {
        this(
                deck.getId(),
                deck.getName(),
                deck.getLanguage(),
                deck.getCategory(),
                deck.getFavorite(),
                deck.getUser().getId(),
                deck.getFlashcards() == null ? 0 : deck.getFlashcards().size(),
                deck.getCreatedAt()
        );
    }
}