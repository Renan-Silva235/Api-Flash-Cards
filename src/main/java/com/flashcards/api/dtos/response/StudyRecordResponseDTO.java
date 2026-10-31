package com.flashcards.api.dtos.response;


import com.flashcards.api.enums.DifficultyLevel;
import com.flashcards.api.enums.StudyResult;

import java.time.LocalDateTime;
import java.util.UUID;

// Resposta de uma revisão: id do card revisado, resultado e a dificuldade que ele tinha antes
public record StudyRecordResponseDTO(

        UUID id,
        StudyResult result,
        DifficultyLevel difficultyLevel,
        LocalDateTime reviewedAt

) {
}
