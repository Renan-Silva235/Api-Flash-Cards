package com.flashcards.api.services;

import com.flashcards.api.dtos.request.RecordReviewRequestDTO;
import com.flashcards.api.entities.FlashCard;
import com.flashcards.api.entities.StudyRecord;
import com.flashcards.api.entities.StudySession;
import com.flashcards.api.enums.CardStatus;
import com.flashcards.api.enums.DifficultyLevel;
import com.flashcards.api.enums.StudyResult;
import com.flashcards.api.exceptions.ResourceNotFoundException;
import com.flashcards.api.repositories.FlashCardRepository;
import com.flashcards.api.repositories.StudyRecordRepository;
import com.flashcards.api.repositories.StudySessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class StudyRecordService {

    @Autowired
    private StudyRecordRepository studyRecordRepository;

    @Autowired
    private StudySessionRepository studySessionRepository;

    @Autowired
    private FlashCardRepository flashCardRepository;

    @Transactional
    public StudyRecord recordReview(UUID sessionId, RecordReviewRequestDTO dto) {
        StudySession session = studySessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão de estudos não encontrada."));

        FlashCard flashcard = flashCardRepository.findById(dto.flashcardId())
                .orElseThrow(() -> new ResourceNotFoundException("Flashcard não encontrado."));

        StudyRecord record = new StudyRecord();
        record.setStudySession(session);
        record.setFlashCard(flashcard);
        record.setResult(dto.result());
        record.setDifficultyLevel(flashcard.getDifficulty());

        switch (dto.result()) {
            case MISTAKE:
                flashcard.setDifficulty(DifficultyLevel.HARD);
                break;

            case DIFFICULT:
                flashcard.setDifficulty(DifficultyLevel.MEDIUM);
                break;

            case HIT:
                flashcard.setDifficulty(DifficultyLevel.EASY);
                break;
        }

        flashCardRepository.save(flashcard);

        return studyRecordRepository.save(record);
    }
}