package com.flashcards.api.entities;

import com.flashcards.api.enums.CardStatus;
import com.flashcards.api.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "flashcards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class FlashCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String translation;

    private String past;
    private String present;
    private String future;

    @Column(name = "example_phrase_1")
    private String examplePhrase1;

    @Column(name = "example_phrase_2")
    private String examplePhrase2;

    @Column(name = "example_phrase_3")
    private String examplePhrase3;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficulty;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardStatus status;

    private Boolean favorite = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = CardStatus.LEARNING;
        }
    }
}
