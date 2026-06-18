package com.flashcards.api.dtos.request;

import com.flashcards.api.enums.StudyResult;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record RecordReviewRequestDTO(
        @NotNull(message = "O ID do flashcard é obrigatório")
        UUID flashcardId,

        @NotNull(message = "O resultado do estudo é obrigatório")
        StudyResult result
) {}
