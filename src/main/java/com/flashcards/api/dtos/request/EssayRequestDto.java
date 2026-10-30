package com.flashcards.api.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EssayRequestDto(
        // Também vai para o prompt da IA: só aceita um nome curto de idioma (ex.: "Italian"),
        // para ninguém conseguir mandar instruções por este campo
        @NotBlank(message = "O idioma é obrigatório")
        @Size(max = 30, message = "Idioma inválido")
        @Pattern(regexp = "^[A-Za-zÀ-ÿ ]+$", message = "Idioma inválido")
        String language,

        @NotBlank(message = "Escreva um texto para corrigir")
        @Size(max = 2000, message = "O texto pode ter no máximo 2000 caracteres")
        String text
) {
}
