package com.flashcards.api.dtos.request;

import com.flashcards.api.enums.DifficultyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateFlashCardRequest(
        @NotBlank(message = "A palavra é obrigatória")
        String word,

        @NotBlank(message = "A tradução é obrigatória")
        String translation,

        String past,
        String present,
        String future,
        String examplePhrase1,
        String examplePhrase2,
        String examplePhrase3,

        @NotNull(message = "A dificuldade é obrigatória")
        DifficultyLevel difficulty,

        Boolean favorite,

        @NotNull(message = "O ID do deck é obrigatório")
        UUID deckId
) {}
