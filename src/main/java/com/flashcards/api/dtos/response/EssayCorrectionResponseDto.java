package com.flashcards.api.dtos.response;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.List;

public record EssayCorrectionResponseDto(
        boolean languageMismatch,
        String detectedLanguage,
        String estimatedLevel,
        String correctedText,
        List<GrammarErrorDto> errors,
        List<NativeSuggestionDto> nativeSuggestions
) {}

record GrammarErrorDto(
        String originalSnippet,
        String correctedSnippet,
        @JsonPropertyDescription("Explanation of the grammar rule and why it was a mistake. MUST be written in Brazilian Portuguese (pt-BR), regardless of the essay's language.")
        String ruleExplanation
) {}

record NativeSuggestionDto(
        String originalSnippet,
        String suggestedSnippet,
        @JsonPropertyDescription("Explanation of why native speakers prefer this phrasing. MUST be written in Brazilian Portuguese (pt-BR), regardless of the essay's language.")
        String explanation
) {}
