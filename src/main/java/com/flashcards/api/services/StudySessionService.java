package com.flashcards.api.services;


import com.flashcards.api.dtos.request.StartSessionRequestDTO;
import com.flashcards.api.entities.Deck;
import com.flashcards.api.entities.StudySession;
import com.flashcards.api.repositories.DeckRepository;
import com.flashcards.api.repositories.StudySessionRepository;
import com.flashcards.api.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StudySessionService {

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Transactional
    public StudySession startSession(StartSessionRequestDTO dto) {
        Deck deck = deckRepository.findById(dto.deckId())
                .orElseThrow(() -> new ResourceNotFoundException("Deck não encontrado com o ID fornecido."));

        StudySession session = new StudySession();
        session.setDeck(deck);
        session.setStartedAt(LocalDateTime.now());

        return studySessionRepository.save(session);
    }

    @Transactional
    public StudySession endSession(UUID sessionId) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão de estudos não encontrada."));

        session.setFinishedAt(LocalDateTime.now());
        return studySessionRepository.save(session);
    }
}
