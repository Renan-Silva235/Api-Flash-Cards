package com.flashcards.api.dtos.request;


import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record StartSessionRequestDTO(
        @NotNull(message = "O ID do deck é obrigatório")
        UUID deckId
) {}