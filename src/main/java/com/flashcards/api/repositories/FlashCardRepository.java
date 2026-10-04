package com.flashcards.api.repositories;

import java.util.Optional;
import com.flashcards.api.entities.FlashCard;
import com.flashcards.api.enums.CardStatus;
import com.flashcards.api.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, UUID> {

    @Query("""
            SELECT f
            FROM FlashCard f
            WHERE f.deck.id = :deckId
            AND f.status IN :status
            ORDER BY
                CASE f.difficulty
                    WHEN com.flashcards.api.enums.DifficultyLevel.HARD THEN 0
                    WHEN com.flashcards.api.enums.DifficultyLevel.MEDIUM THEN 1
                    WHEN com.flashcards.api.enums.DifficultyLevel.EASY THEN 2
                END,
                f.createdAt ASC
""")
    List<FlashCard> findByDeckIdAndStatusIn(
            @Param("deckId") UUID deckId,
            @Param("status") List<CardStatus> status
    );

    List<FlashCard> findByDeckId(@Param("deckId") UUID deckId);

    // Busca só nos cards dos decks do usuário
    @Query("SELECT f FROM FlashCard f WHERE f.deck.user.id = :userId AND (" +
            "LOWER(f.word) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.translation) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<FlashCard> searchCardsByUser(@Param("query") String query, @Param("userId") UUID userId);

    // Só encontra o card se o deck dele pertencer ao usuário informado
    Optional<FlashCard> findByIdAndDeckUserId(UUID id, UUID userId);

    long countByDeckLanguage(String language);

    long countByDeckLanguageAndDifficulty(String language, DifficultyLevel difficulty);

    long count();

    long countByDifficulty(DifficultyLevel difficulty);

    long countByDeckUserId(UUID userId);

    long countByDeckUserIdAndDifficulty(UUID userId, DifficultyLevel difficulty);

    long countByDeckUserIdAndDeckLanguage(UUID userId, String language);

    long countByDeckUserIdAndDeckLanguageAndDifficulty(
            UUID userId,
            String language,
            DifficultyLevel difficulty
    );
}
