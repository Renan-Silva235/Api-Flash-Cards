package com.flashcards.api.dtos.response;

public record StatisticsResponseDTO(
        long totalCards,
        long easy,
        long medium,
        long hard
) {
}