package com.flashcards.api.repositories;


import com.flashcards.api.entities.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, UUID> {

    // Só encontra a sessão se ela pertencer ao usuário informado
    Optional<StudySession> findByIdAndUserId(UUID id, UUID userId);
}
