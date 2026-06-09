package com.flashcards.api.dtos.response;

import com.flashcards.api.entities.FlashCard;
import com.flashcards.api.enums.CardStatus;
import com.flashcards.api.enums.DifficultyLevel;

import java.time.LocalDateTime;
import java.util.UUID;

public record FlashCardResponse(
        UUID id,
        String word,
        String translation,
        String past,
        String present,
        String future,
        String examplePhrase1,
        String examplePhrase2,
        String examplePhrase3,
        DifficultyLevel difficulty,
        CardStatus status,
        Boolean favorite,
        LocalDateTime createdAt
) {
    public FlashCardResponse(FlashCard card) {
        this(
                card.getId(), card.getWord(), card.getTranslation(),
                card.getPast(), card.getPresent(), card.getFuture(),
                card.getExamplePhrase1(), card.getExamplePhrase2(), card.getExamplePhrase3(),
                card.getDifficulty(), card.getStatus(), card.getFavorite(), card.getCreatedAt()
        );
    }
}
