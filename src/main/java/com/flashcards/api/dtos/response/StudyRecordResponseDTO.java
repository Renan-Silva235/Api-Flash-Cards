package com.flashcards.api.dtos.response;


import com.flashcards.api.entities.StudyRecord;
import com.flashcards.api.enums.DifficultyLevel;
import com.flashcards.api.enums.StudyResult;

import java.time.LocalDateTime;
import java.util.UUID;

public record StudyRecordResponseDTO(

        UUID id,
        StudyResult result,
        DifficultyLevel difficultyLevel,
        LocalDateTime reviewedAt

) {

    public StudyRecordResponseDTO(StudyRecord record) {
        this(
                record.getId(),
                record.getResult(),
                record.getDifficultyLevel(),
                record.getReviewedAt()
        );
    }
}