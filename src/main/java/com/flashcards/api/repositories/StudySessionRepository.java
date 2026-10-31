package com.flashcards.api.repositories;


import com.flashcards.api.entities.StudySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, UUID> {

    // Só encontra a sessão se ela pertencer ao usuário informado
    Optional<StudySession> findByIdAndUserId(UUID id, UUID userId);

    Optional<StudySession> findByUserIdAndDeckId(UUID userId, UUID deckId);

    // Cria a sessão do usuário no deck ou reinicia a existente. Feito num único
    // comando para dois "start" simultâneos não violarem a chave única.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = """
            INSERT INTO study_sessions (user_id, deck_id, started_at, finished_at, easy, medium, hard)
            VALUES (:userId, :deckId, :startedAt, NULL, 0, 0, 0)
            ON CONFLICT (user_id, deck_id) DO UPDATE SET
                started_at = EXCLUDED.started_at,
                finished_at = NULL,
                easy = 0,
                medium = 0,
                hard = 0
            """, nativeQuery = true)
    void startOrRestart(@Param("userId") UUID userId,
                        @Param("deckId") UUID deckId,
                        @Param("startedAt") LocalDateTime startedAt);
}
