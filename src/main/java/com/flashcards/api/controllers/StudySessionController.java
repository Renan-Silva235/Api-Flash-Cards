package com.flashcards.api.controllers;

import com.flashcards.api.dtos.request.RecordReviewRequestDTO;
import com.flashcards.api.dtos.request.StartSessionRequestDTO;
import com.flashcards.api.dtos.response.StudyRecordResponseDTO;
import com.flashcards.api.dtos.response.StudySessionResponseDTO;
import com.flashcards.api.entities.StudyRecord;
import com.flashcards.api.entities.StudySession;
import com.flashcards.api.services.StudyRecordService;
import com.flashcards.api.services.StudySessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/study-sessions")
public class StudySessionController {

    @Autowired
    private StudySessionService studySessionService;

    @Autowired
    private StudyRecordService studyRecordService;

    @PostMapping("/start")
    public ResponseEntity<StudySessionResponseDTO> startSession(@RequestBody @Valid StartSessionRequestDTO dto) {
        StudySession session = studySessionService.startSession(dto);
        return ResponseEntity.ok(new StudySessionResponseDTO(session));
    }

    @PostMapping("/{id}/review")
    public ResponseEntity<StudyRecordResponseDTO> recordReview(
            @PathVariable UUID id,
            @RequestBody @Valid RecordReviewRequestDTO dto) {

        StudyRecord record = studyRecordService.recordReview(id, dto);

        return ResponseEntity.ok(new StudyRecordResponseDTO(record));
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<StudySession> endSession(@PathVariable UUID id) {
        StudySession session = studySessionService.endSession(id);
        return ResponseEntity.ok(session);
    }
}
