package com.flashcards.api.services;

import com.flashcards.api.dtos.request.RecordReviewRequestDTO;
import com.flashcards.api.dtos.response.StudyRecordResponseDTO;
import com.flashcards.api.entities.FlashCard;
import com.flashcards.api.entities.StudySession;
import com.flashcards.api.enums.DifficultyLevel;
import com.flashcards.api.exceptions.ResourceNotFoundException;
import com.flashcards.api.repositories.FlashCardRepository;
import com.flashcards.api.repositories.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StudyRecordService {

    @Autowired
    private StudySessionService studySessionService;

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private FlashCardRepository flashCardRepository;

    @Transactional
    public StudyRecordResponseDTO recordReview(UUID sessionId, RecordReviewRequestDTO dto) {
        // A sessão precisa ser do usuário logado...
        StudySession session = studySessionService.findOwnedSession(sessionId);

        // ...e o card precisa ser do mesmo deck da sessão
        FlashCard flashcard = flashCardRepository.findById(dto.flashcardId())
                .filter(card -> card.getDeck().getId().equals(session.getDeck().getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard não encontrado."));

        // Em vez de gravar uma linha por revisão, atualiza os contadores do card e da sessão
        DifficultyLevel previousDifficulty = flashcard.getDifficulty();
        LocalDateTime reviewedAt = LocalDateTime.now();

        flashcard.setReviewCount(flashcard.getReviewCount() + 1);
        flashcard.setLastReviewedAt(reviewedAt);

        switch (dto.result()) {
            case MISTAKE:
                flashcard.setDifficulty(DifficultyLevel.HARD);
                flashcard.setHardCount(flashcard.getHardCount() + 1);
                session.setHard(session.getHard() + 1);
                break;

            case DIFFICULT:
                flashcard.setDifficulty(DifficultyLevel.MEDIUM);
                flashcard.setMediumCount(flashcard.getMediumCount() + 1);
                session.setMedium(session.getMedium() + 1);
                break;

            case HIT:
                flashcard.setDifficulty(DifficultyLevel.EASY);
                flashcard.setEasyCount(flashcard.getEasyCount() + 1);
                session.setEasy(session.getEasy() + 1);
                break;
        }

        flashCardRepository.save(flashcard);
        studySessionRepository.save(session);

        return new StudyRecordResponseDTO(flashcard.getId(), dto.result(), previousDifficulty, reviewedAt);
    }
}
