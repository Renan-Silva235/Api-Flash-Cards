package com.flashcards.api.repositories;

import com.flashcards.api.entities.FlashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, UUID> {

    List<FlashCard> findByDeckId(UUID deckId);

    @Query("SELECT f FROM FlashCard f WHERE LOWER(f.word) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(f.translation) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<FlashCard> searchCards(@Param("query") String query);
}
