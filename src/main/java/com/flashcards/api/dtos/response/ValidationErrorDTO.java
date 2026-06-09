package com.flashcards.api.dtos.response;

import org.springframework.validation.FieldError;

public record ValidationErrorDTO(String field, String message) {
    public ValidationErrorDTO(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}