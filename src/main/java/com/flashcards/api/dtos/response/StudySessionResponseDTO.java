package com.flashcards.api.dtos.response;


import com.flashcards.api.entities.StudySession;

import java.time.LocalDateTime;
import java.util.UUID;

public record StudySessionResponseDTO(
        UUID id,
        LocalDateTime startedAt,
        LocalDateTime finishedAt
) {
    public StudySessionResponseDTO(StudySession session) {
        this(
                session.getId(),
                session.getStartedAt(),
                session.getFinishedAt()
        );
    }
}