package com.flashcards.api.repositories;

import com.flashcards.api.entities.StudyRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudyRecordRepository extends JpaRepository<StudyRecord, UUID> {
}
