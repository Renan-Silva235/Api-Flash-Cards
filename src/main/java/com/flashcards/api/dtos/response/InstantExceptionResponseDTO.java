package com.flashcards.api.dtos.response;


import java.time.Instant;

public record InstantExceptionResponseDTO(
        Instant timestamp,
        Integer status,
        String error,
        String message,
        String path
) {}
