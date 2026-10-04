package com.flashcards.api.services;


import com.flashcards.api.security.CurrentUserService;
import com.flashcards.api.dtos.request.StartSessionRequestDTO;
import com.flashcards.api.entities.Deck;
import com.flashcards.api.entities.StudySession;
import com.flashcards.api.repositories.StudySessionRepository;
import com.flashcards.api.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flashcards.api.entities.User;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private DeckService deckService;

    @Autowired
    private CurrentUserService currentUserService;

    /** Sessão do usuário logado; de outro usuário responde "não encontrada". */
    @Transactional(readOnly = true)
    public StudySession findOwnedSession(UUID sessionId) {
        return studySessionRepository.findByIdAndUserId(sessionId, currentUserService.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Sessão de estudos não encontrada."));
    }

    @Transactional
    public StudySession startSession(StartSessionRequestDTO dto) {
        // Só deixa estudar deck do próprio usuário
        Deck deck = deckService.findOwnedDeck(dto.deckId());
        User user = currentUserService.getCurrentUser();

        StudySession session = new StudySession();
        session.setDeck(deck);
        session.setUser(user);
        session.setStartedAt(LocalDateTime.now());
        return studySessionRepository.save(session);
    }

    @Transactional
    public StudySession endSession(UUID sessionId) {
        StudySession session = findOwnedSession(sessionId);

        session.setFinishedAt(LocalDateTime.now());
        return studySessionRepository.save(session);
    }
}
