package com.flashcards.api.entities;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "study_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class StudySession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // A sessão é uma por usuário + deck e é reaproveitada a cada novo estudo
    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    // Placar da sessão atual (zerado a cada início).
    // Fácil = HIT, Médio = DIFFICULT, Difícil = MISTAKE
    @Column(nullable = false)
    private Integer easy = 0;

    @Column(nullable = false)
    private Integer medium = 0;

    @Column(nullable = false)
    private Integer hard = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @PrePersist
    protected void onCreate() {
        this.startedAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
