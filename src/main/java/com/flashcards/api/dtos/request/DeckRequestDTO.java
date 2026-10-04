package com.flashcards.api.dtos.request;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

public record DeckRequestDTO(
        @NotBlank(message = "O nome do deck é obrigatório")
        String name,

        @NotBlank(message = "O idioma é obrigatório")
        String language,

        @NotBlank(message = "A categoria é obrigatória")
        String category,

        // Ignorado pela API: o dono do deck é sempre o usuário do token.
        // Mantido opcional só para não quebrar clientes que ainda enviam o campo.
        UUID userId
) {}
