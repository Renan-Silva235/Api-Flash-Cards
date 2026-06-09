package com.flashcards.api.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DeckRequestDTO(
        @NotBlank(message = "O nome do deck é obrigatório")
        String name,

        @NotBlank(message = "O idioma é obrigatório")
        String language,

        @NotBlank(message = "A categoria é obrigatória")
        String category,

        @NotNull(message = "O ID do usuário é obrigatório")
        UUID userId
) {}
