package com.flashcards.api.repositories;

import com.flashcards.api.entities.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeckRepository extends JpaRepository<Deck, UUID> {
    @Query(
    """
        SELECT d
        FROM Deck d
        WHERE d.user.id = :userId
        ORDER BY d.favorite DESC, d.createdAt ASC
    """)
    List<Deck> findByUserId(@Param("userId") UUID userId);
    long countByUserId(UUID userId);

    long countByUserIdAndFavoriteTrue(UUID userId);
}
